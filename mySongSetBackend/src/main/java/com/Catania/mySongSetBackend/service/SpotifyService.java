package com.Catania.mySongSetBackend.service;

import com.Catania.mySongSetBackend.dto.SongDTO;
import com.Catania.mySongSetBackend.model.Artist;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.json.JSONObject;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpotifyService {
    @Value("${SPOTIFY_CLIENT_ID}")
    private String clientId;

    @Value("${SPOTIFY_CLIENT_SECRET}")
    private String clientSecret;

    @Value("${SPOTIFY_API_URL}")
    private String spotifyApiUrl;

    @Value("${SPOTIFY_API_AUTH}")
    private String spotifyAuthUrl;

    private RestTemplate restTemplate = new RestTemplate();

    public String getAccessToken() {
        String credentials = clientId + ":" + clientSecret;
        String encodedCredentials = java.util.Base64.getEncoder().encodeToString(credentials.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "Basic " + encodedCredentials);

        HttpEntity<String> request = new HttpEntity<>("grant_type=client_credentials", headers);

        ResponseEntity<String> response = restTemplate.exchange(
                spotifyAuthUrl, HttpMethod.POST, request, String.class);

        JSONObject json = new JSONObject(response.getBody());
        return json.getString("access_token");
    }

    public Artist[] searchArtist(String artistName) {
        String token = getAccessToken();
        int limit = 5; //to limit the objects that spotify API will send us
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        HttpEntity<String> request = new HttpEntity<>(headers);

        String url = spotifyApiUrl + "/search?q=" + artistName + "&type=artist&limit=" + limit;

        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, request, String.class);

        JSONObject json = new JSONObject(response.getBody());

        return json.getJSONObject("artists").getJSONArray("items").toList().stream()
                .map(item -> {
                    JSONObject artistJson = new JSONObject((java.util.Map) item);
                    Artist artist = new Artist();
                    artist.setId(artistJson.getString("id"));
                    artist.setName(artistJson.getString("name"));
                    artist.setPopularity(artistJson.getInt("popularity"));
                    artist.setGenres(artistJson.getJSONArray("genres").toList()
                            .toArray(new String[0]));
                    return artist;
                })
                .toArray(Artist[]::new);
    }

    public List<SongDTO> searchTracks(String query) {
        String token = getAccessToken();
        int limit = 10;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        HttpEntity<String> request = new HttpEntity<>(headers);

        String searchUrl = spotifyApiUrl + "/search?q=" + query + "&type=track&market=US&limit=" + limit;

        try {
            ResponseEntity<String> response = restTemplate.exchange(searchUrl, HttpMethod.GET, request, String.class);

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new RuntimeException("Failed to search tracks from Spotify: " + response.getStatusCode());
            }

            JSONObject json = new JSONObject(response.getBody());

            if (!json.has("tracks")) {
                throw new RuntimeException("No 'tracks' field in response from Spotify API");
            }

            JSONArray tracksJsonArray = json.getJSONObject("tracks").getJSONArray("items");

            return tracksJsonArray.toList().stream()
                    .map(item -> {
                        JSONObject trackJson = new JSONObject((java.util.Map) item);
                        return mapToSongDTO(trackJson);
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error searching tracks: " + e.getMessage(), e);
        }
    }



    public List<SongDTO> getTopTracks(String artistId) {
        String token = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        HttpEntity<String> request = new HttpEntity<>(headers);

        String url = spotifyApiUrl + "/artists/" + artistId + "/top-tracks?market=US";

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new RuntimeException("Failed to fetch top tracks from Spotify: " + response.getStatusCode());
            }

            JSONObject json = new JSONObject(response.getBody());

            if (!json.has("tracks")) {
                throw new RuntimeException("No 'tracks' field in response from Spotify API");
            }

            JSONArray tracksJsonArray = json.getJSONArray("tracks");

            return tracksJsonArray.toList().stream()
                    .map(item -> {
                        JSONObject trackJson = new JSONObject((java.util.Map) item);
                        return mapToSongDTO(trackJson);
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching top tracks: " + e.getMessage(), e);
        }
    }


    private SongDTO mapToSongDTO(JSONObject trackJson) {
        SongDTO songDTO = new SongDTO();
        songDTO.setTitle(trackJson.getString("name"));
        songDTO.setArtist(trackJson.getJSONArray("artists").getJSONObject(0).getString("name"));
        songDTO.setAlbum(trackJson.getJSONObject("album").getString("name"));
        songDTO.setImageUrl(trackJson.getJSONObject("album").getJSONArray("images")
                .getJSONObject(0).getString("url"));
        songDTO.setDuration(convertMillisToTime(trackJson.getInt("duration_ms")));
        songDTO.setManual(false);

        return songDTO;
    }


    private Time convertMillisToTime(int durationMs) {
        if (durationMs < 0) {
            throw new IllegalArgumentException("Invalid duration_ms value: " + durationMs);
        }
        long minutes = durationMs / 60000;
        long seconds = (durationMs % 60000) / 1000;
        return Time.valueOf(LocalTime.of((int) minutes, (int) seconds));
    }

}
