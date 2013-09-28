package ioio.examples.hello;
 
import ioio.examples.hello.R;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.AbstractIOIOActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.ToggleButton;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
 
public class MainActivity extends AbstractIOIOActivity {

private SeekBar servoControl;
private SeekBar speedControl;

private TextView varField;
private TextView speedField;

private volatile float varValue;
private volatile float speedValue;
private volatile float speed;

private ToggleButton stopButton;
private Button left, right,forward;

@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.main);

servoControl = (SeekBar) findViewById(R.id.seekBarVar);
speedControl = (SeekBar) findViewById(R.id.seekBarVar2);

varField = (TextView) findViewById(R.id.textViewVarVal);
speedField = (TextView) findViewById(R.id.textViewVarVal2);

stopButton = (ToggleButton)findViewById(R.id.stopButton);
left = (Button)findViewById(R.id.leftButton);
right = (Button)findViewById(R.id.rightButton);
forward = (Button)findViewById(R.id.forwardButton);

servoControl.setMax(100);
speedControl.setMax(100);

servoControl.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
@Override
public void onStopTrackingTouch(SeekBar seekBar) {
}
 
@Override
public void onStartTrackingTouch(SeekBar seekBar) {
}
 
@Override
public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
varValue = (float)progress / (float)servoControl.getMax();
varField.setText(String.valueOf(varValue));
}
});

left.setOnClickListener(new View.OnClickListener(){
	@Override
	public void onClick(View v){
		varValue = 0.4f;
	}
});

right.setOnClickListener(new View.OnClickListener(){
	@Override
	public void onClick(View v){
		varValue = 0.9f;
	}
});

forward.setOnClickListener(new View.OnClickListener(){
	@Override
	public void onClick(View v){
		varValue = 0.7f;
	}
});

speedControl.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
@Override
public void onStopTrackingTouch(SeekBar seekBar) {
}
 
@Override
public void onStartTrackingTouch(SeekBar seekBar) {
}
 
@Override
public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
speedValue = (float)progress / (float)speedControl.getMax();
speedField.setText(String.valueOf(speedValue));
}
});

}

class IOIOThread extends AbstractIOIOActivity.IOIOThread {

private PwmOutput led;

private PwmOutput servo;

private PwmOutput pwm;

private DigitalOutput dir;
 

@Override
protected void setup() throws ConnectionLostException {
led = ioio_.openPwmOutput(0, 300);
servo = ioio_.openPwmOutput(6, 50);
pwm = ioio_.openPwmOutput(10, 100);
dir = ioio_.openDigitalOutput(11);
}
 

@Override
protected void loop() throws ConnectionLostException {
servo.setDutyCycle(0.05f + varValue * 0.05f);
led.setDutyCycle(1 - varValue);

if (stopButton.isChecked()){
	speed = 0;
}else{
	speed = speedValue;
}

pwm.setDutyCycle(speed);
dir.write(false);
}
}

@Override
protected AbstractIOIOActivity.IOIOThread createIOIOThread() {
return new IOIOThread();
}
}