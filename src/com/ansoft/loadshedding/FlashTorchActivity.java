package com.ansoft.loadshedding;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.List;

public class FlashTorchActivity extends Activity implements
		SurfaceHolder.Callback {

	private static final String TAG = "FlashTorchAtivity";

	private Camera camera;
	private boolean lightOn;
	private boolean previewOn;
	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getSupportActionBar().hide();
		// getWindow().setFlags(
		// WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		WindowManager.LayoutParams layout = getWindow().getAttributes();
		layout.screenBrightness = 1F;
		getWindow().setAttributes(layout);
		setContentView(R.layout.activity_flash_torch);
		surfaceView = (SurfaceView) this.findViewById(R.id.surfaceview);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		((ToggleButton) findViewById(R.id.toggleButton))
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							turnLightOn();
						} else {
							turnLightOff();
						}
					}
				});
	}

	private Camera initCamera() {
		if (camera == null) {
			try {
				camera = Camera.open();
			} catch (RuntimeException e) {
				// no camera support. ignore
			}
		}
		return camera;
	}

	@Override
	public void onStart() {
		super.onStart();
		startPreview();
	}

	private void turnLightOn() {
		if (camera == null) {
			return;
		}
		lightOn = true;
		Camera.Parameters parameters = camera.getParameters();
		if (parameters == null) {
			return;
		}
		List<String> flashModes = parameters.getSupportedFlashModes();
		// Check if camera flash exists
		if (flashModes == null) {
			return;
		}
		String flashMode = parameters.getFlashMode();
		if (!Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode)) {
			// Turn on the flash
			if (flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
				camera.setParameters(parameters);
			} else {
				// flash torch is not supported
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);

				camera.setParameters(parameters);
				try {
					camera.autoFocus(new AutoFocusCallback() {
						@Override
						public void onAutoFocus(boolean success, Camera camera) {
							int count = 1;
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void turnLightOff() {
		if (lightOn) {
			// set the background to dark
			lightOn = false;
			if (camera == null) {
				return;
			}
			Camera.Parameters parameters = camera.getParameters();
			if (parameters == null) {
				return;
			}
			List<String> flashModes = parameters.getSupportedFlashModes();
			String flashMode = parameters.getFlashMode();
			// Check if camera flash exists
			if (flashModes == null) {
				return;
			}
			if (!Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)) {
				// Turn off the flash
				if (flashModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
					parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
					camera.setParameters(parameters);
				} else {
					// flash torch off is not supported
				}
			}
		}
	}

	private void startPreview() {
		if (!previewOn && camera != null) {
			camera.startPreview();
			previewOn = true;
		}
	}

	private void stopPreview() {
		if (previewOn && camera != null) {
			camera.stopPreview();
			previewOn = false;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		initCamera();
		turnLightOn();
	}

	@Override
	public void onPause() {
		super.onPause();
		turnLightOff();
		((ToggleButton) findViewById(R.id.toggleButton)).setChecked(false);
	}

	@Override
	public void onStop() {
		super.onStop();
		if (camera != null) {
			stopPreview();
			camera.release();
			camera = null;
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			if (camera != null) {
				camera.setPreviewDisplay(holder);
				startPreview();
				turnLightOn();
			}
		} catch (IOException e) {
			// ignore
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}

}
