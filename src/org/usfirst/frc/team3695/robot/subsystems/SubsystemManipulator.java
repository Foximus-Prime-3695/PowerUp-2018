package org.usfirst.frc.team3695.robot.subsystems;

import org.usfirst.frc.team3695.robot.Constants;
import org.usfirst.frc.team3695.robot.util.Xbox;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Subsystem;

/** VROOM VROOM */
public class SubsystemManipulator extends Subsystem {
	
	
	private TalonSRX armLeft;
	private TalonSRX armRight;
	
	public Boolean redlining;
	
	public long redlineTime;
	
	/** applies left arm motor invert */
	public static final double leftArmify(double left) {
		return left * (Constants.LEFT_ARM_MOTOR_INVERT ? -1.0 : 1.0);
	}
	
	/** applies right arm motor invert */
	public static final double rightArmify(double right) {
		return right * (Constants.RIGHT_ARM_MOTOR_INVERT ? -1.0 : 1.0);
	}
	
	/** runs at robot boot */
    public void initDefaultCommand() {}
	
	/** gives birth to the CANTalons */
    public SubsystemManipulator(){
    	armLeft = new TalonSRX(Constants.LEFT_ARM);
    	armRight = new TalonSRX(Constants.RIGHT_ARM);
    }
    
    /** eat the power cube */
    public void eat(double speed) {
    	speed *= -1;
    	armLeft.set(ControlMode.PercentOutput, leftArmify(speed));
    	armRight.set(ControlMode.PercentOutput, rightArmify(speed));
    }
    
    /** spit out the power cube */
    public void spit(double speed) {
    	armLeft.set(ControlMode.PercentOutput, leftArmify(speed));
    	armRight.set(ControlMode.PercentOutput, rightArmify(speed));
    }
    
    /** STOP SPINNING ME RIGHT ROUND, BABY RIGHT ROUND */
    public void stopSpinning() {
    	armLeft.set(ControlMode.PercentOutput, 0);
    	armRight.set(ControlMode.PercentOutput, 0);
    }
    
    /** imitates an engine revving and idling */
    public void rev(Joystick joy) {
    	double intensity = Math.abs(Math.sqrt((Math.pow(Xbox.LEFT_X(joy), 2) + Math.pow(Xbox.LEFT_X(joy), 2))));
    	
    	int rpm = (int) ((((double) Constants.REDLINE - (double) Constants.IDLE) * intensity) + Constants.IDLE);
    	int miliseconds = (1 / rpm) * 60000;
    	
    	
    	if (!redlining) { redlineTime = System.currentTimeMillis(); redlining = true; }
    		else if (System.currentTimeMillis() - redlineTime >= miliseconds) { redlining = false; }
    	
    	double speed = (System.currentTimeMillis() - redlineTime) / (double) miliseconds;
    	speed = speed > 1.0 ? 1.0 : speed;
    	
    	speed = generateCurve(speed, 0, .25 * (intensity * .8 + .2), (intensity * .8 + .2));
    	
    	armLeft.set(ControlMode.PercentOutput, leftArmify(speed));
    	armRight.set(ControlMode.PercentOutput, rightArmify(speed));
    }

    
    /** generates a quadratic curve based on the three points in constants */
    public double generateRedlineCurve(double x) {
    	// TODO simplify this; I just plugged our variables into the equation for this
    	double y;
    	y  = Constants.REDLINE_START * (((x - .5) * (x - 1))/(.5));
    	y += Constants.REDLINE_MID * ((x * (x - 1))/(-.25));
    	y += Constants.REDLINE_END * ((x * (x-.5))/(.5));
    	return y;
    }
    
    /** generates a quadratic curve based on the three points given */
    public double generateCurve(double x, double start, double mid, double end) {
    	// TODO simplify this; I just plugged our variables into the equation for this
    	double y;
    	y  = start * (((x - .5) * (x - 1))/(.5));
    	y += mid * ((x * (x - 1))/(-.25));
    	y += end * ((x * (x-.5))/(.5));
    	return y;
    }

    /** configures the voltage of each CANTalon */
    private void voltage(TalonSRX talon) {
    	// talon.configNominalOutputVoltage(0f, 0f);
    	// talon.configPeakOutputVoltage(12.0f, -12.0f);
    	// talon.enableCurrentLimit(true);
    	// talon.configContinuousCurrentLimit(30, 3000);
    }
    
    

}
