package me.selinali.tribble.ui.shot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.parceler.Parcels;

import me.selinali.tribble.R;
import me.selinali.tribble.model.Shot;

public class ShotActivity extends AppCompatActivity {

  private static final String EXTRA_SHOT = "EXTRA_SHOT";

  public static Intent launchIntentFor(Shot shot, Context context) {
    return new Intent(context, ShotActivity.class)
        .putExtra(EXTRA_SHOT, Parcels.wrap(shot));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_shot);
  }
}
