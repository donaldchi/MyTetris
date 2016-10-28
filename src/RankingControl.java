import java.io.File;
import java.io.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RankingControl {

	public class RankInfo {
		private int rank;
		private String name;
		private int score;

		private RankInfo(int rank, String name, int score) {
			this.rank = rank;
			this.name = name;
			this.score = score;
		}

		public void setInfo(int rank, String name, int score) {
			this.rank = rank;
			this.name = name;
			this.score = score;
		}

		public int getRank() {
			return rank;
		}

		public String getName() {
			return name;
		}

		public int getScore() {
			return score;
		}
	}

	public static RankInfo [] rankInfo;
	JsonNode root;

	public RankingControl() {
		rankInfo = new RankInfo[4];
	}

	public void read () {
		try{
			ObjectMapper mapper = new ObjectMapper();
			root = mapper.readTree(new File("rank/rank.json"));
			// Get Contact
			JsonNode rankingNode = root.path("ranking");
			int index = 0;
			for (JsonNode node : rankingNode) {
				int rank = node.path("rank").asInt();
				String name = node.path("name").asText();
				int score = node.path("score").asInt();
				rankInfo[index] = new RankInfo(rank, name, score);
				index++;
			}
		}  catch (Exception e) {
			System.out.println("Error!!");
		}
	}

	public void write() {
		try{
			JSONObject jsonObject = new JSONObject();
		    JSONArray jsonArary = new JSONArray();
		     
		    // jsonデータの作成
		    JSONObject jsonOneData;
		    for (int i=0; i<4; i++) {
			    jsonOneData = new JSONObject();
			    jsonOneData.put("score", rankInfo[i].getScore());
			    jsonOneData.put("name", rankInfo[i].getName());
			    jsonOneData.put("rank", rankInfo[i].getRank());
			    jsonArary.put(jsonOneData);
		    }
		     
		    jsonObject.put("ranking", jsonArary);
		     
		    // jsonファイル出力
		    File file = new File("rank/rank.json");
		    FileWriter filewriter;
		 
		    filewriter = new FileWriter(file);
		    BufferedWriter bw = new BufferedWriter(filewriter);
		    PrintWriter pw = new PrintWriter(bw);
		    pw.write(jsonObject.toString());
		    pw.close();

		}  catch (Exception e) {
			System.out.println("Write Error!!");
		}
	}

	public static void main(String[] args) {
		RankingControl rc = new RankingControl();
		rc.read();
		rc.rankInfo[0].setInfo(0, "dd", 5000);	 
		rc.write();
	}
}