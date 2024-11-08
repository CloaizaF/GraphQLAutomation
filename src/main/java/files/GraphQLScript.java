package files;

import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import static io.restassured.RestAssured.given;

public class GraphQLScript {
    public static void main(String[] args) {

        // Query
        int characterId = 10206;
        String queryResponse = given().log().all().header("Content-Type", "application/json")
                .body("{\"query\":\"query ($characterId: Int!, $episodeId: Int!) {\\n  character(characterId: $characterId) {\\n    name\\n    gender\\n    status\\n    id\\n  }\\n  location(locationId: 15186) {\\n    name\\n    dimension\\n  }\\n  episode(episodeId: $episodeId) {\\n    name\\n    air_date\\n    episode\\n  }\\n  characters(filters: {name: \\\"Rahul\\\"}) {\\n    info {\\n      count\\n    }\\n    result {\\n      name\\n      type\\n      id\\n      location {\\n        id\\n      }\\n      episodes {\\n        id\\n        name\\n      }\\n    }\\n  }\\n  episodes(filters: {episode: \\\"hulu\\\"}) {\\n    info {\\n      count\\n    }\\n    result {\\n      id\\n      name\\n      air_date\\n      episode\\n    }\\n  }\\n}\",\"variables\":{\"characterId\":"+ characterId +",\"episodeId\":11352}}")
                .when().post("https://rahulshettyacademy.com/gq/graphql")
                .then().extract().asString();

        System.out.println(queryResponse);
        JsonPath jsonQueryResponse = new JsonPath(queryResponse);
        String characterName = jsonQueryResponse.getString("data.character.name");
        Assert.assertEquals(characterName, "Robin");

        // Mutation
        String newCharacter = "Denisse";
        String mutationResponse = given().log().all().header("Content-Type", "application/json")
                .body("{\"query\":\"mutation {\\n  createLocation(location: {name: \\\"Aus\\\", type: \\\"Southzone\\\", dimension: \\\"234\\\"}) {\\n    id\\n  }\\n  createCharacter(character: {name: \\\"Robin\\\", type: \\\"Macho\\\", status: \\\"dead\\\", species: \\\"fantasy\\\", gender: \\\"male\\\", image: \\\"png\\\", originId: 15185, locationId: 15185}) {\\n    id\\n  }\\n  createEpisode(episode: {name: \\\"The dark one\\\", air_date: \\\"11-11-11\\\", episode: \\\"mulup\\\"}) {\\n    id\\n  }\\n  deleteLocations(locationIds: [15187]) {\\n    locationsDeleted\\n  }\\n}\",\"variables\":{\"locationName\":\"New Zealand\",\"characterName\":\"" + newCharacter + "\",\"episodeName\":\"Manifest\"}}")
                .when().post("https://rahulshettyacademy.com/gq/graphql")
                .then().extract().asString();

        System.out.println(mutationResponse);
        JsonPath jsonMutationResponse = new JsonPath(mutationResponse);
        int locationsDeleted = jsonMutationResponse.getInt(("data.deleteLocations.locationsDeleted"));
        Assert.assertEquals(locationsDeleted, 0);


    }
}
