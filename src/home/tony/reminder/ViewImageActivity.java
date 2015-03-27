package home.tony.reminder;

import home.tony.reminder.bitmap.ImageLoder;
import home.tony.reminder.common.Consts;

import java.io.File;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class ViewImageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imag_view);
		String path = getIntent().getStringExtra(Consts.REQUEST);
		ImageView imageView = (ImageView) findViewById(R.id.imageView);
		File file = new File(path);
		if (file.exists()) {
			imageView.setImageBitmap(BitmapFactory.decodeFile(path));
			imageView.setImageBitmap(ImageLoder.decodeSampledBitmapFromResource(path, 100, 100));
		}

		this.getActionBar().hide();
	}



}
