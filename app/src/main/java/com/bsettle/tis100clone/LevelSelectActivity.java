package com.bsettle.tis100clone;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.bsettle.tis100clone.level.LevelGridAdapter;
import com.bsettle.tis100clone.level.LevelTileInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Logger;

public class LevelSelectActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private Logger logger = Logger.getLogger("LevelSelectActivity");

    private GridView levelGrid;
    private LevelGridAdapter levelGridAdapter;
    private ArrayList<LevelTileInfo> levels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);

        levelGrid = findViewById(R.id.levelGrid);
        levelGrid.getWidth();

        levelGrid.setHorizontalSpacing(80);
        levelGrid.setVerticalSpacing(80);

        levelGrid.setOnItemClickListener(this);

        parseLevelNames();
        setDataAdapter();
        openLevel(levels.get(1));
    }

    private void openLevel(LevelTileInfo item){
        Intent intent = new Intent(LevelSelectActivity.this, LevelActivity.class);
        intent.putExtra("level", item);
        startActivity(intent);
    }

    private void parseLevelNames(){
        AssetManager assetManager = getAssets();

        try {
            InputStream is = assetManager.open("levels.json");
            JsonReader reader = new JsonReader(new InputStreamReader(is));
            reader.setLenient(true);
            reader.beginArray();
            int levelCount = 0;
            while (reader.hasNext()) {
                LevelTileInfo item = parseLevel(levelCount, reader);
                levels.add(item);
                levelCount++;
            }
            reader.endArray();
            reader.close();
            is.close();
        } catch (IOException e) {
            logger.info(e.getMessage());
        }

    }

    private LevelTileInfo parseLevel(int num, JsonReader reader) throws IOException{
        reader.beginObject();
        String level_name = "", description = "";

        while(reader.hasNext()){
            String name = reader.nextName();
            switch(name) {
                case "name":
                    level_name = reader.nextString();
                    break;
                case "description":
                    description = reader.nextString();
                    break;
                default:
                    String val = reader.nextString();
                    logger.info("UNKNOWN : " + name + "->" + val);
            }
        }
        reader.endObject();
        return new LevelTileInfo(num, level_name, description);
    }

    // Set the Data Adapter
    private void setDataAdapter()
    {
        levelGridAdapter = new LevelGridAdapter(getApplicationContext(), R.layout.level_tile, levels);
        levelGrid.setAdapter(levelGridAdapter);
    }

    @Override
    public void onItemClick(final AdapterView<?> arg0, final View view, final int position, final long id)
    {
        openLevel(levels.get(position));
    }
}
