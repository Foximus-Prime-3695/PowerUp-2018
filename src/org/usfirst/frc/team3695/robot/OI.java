package org.usfirst.frc.team3695.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team3695.robot.commands.*;
import org.usfirst.frc.team3695.robot.enumeration.Mast;
import org.usfirst.frc.team3695.robot.enumeration.Position;
import org.usfirst.frc.team3695.robot.util.Util;
import org.usfirst.frc.team3695.robot.util.Xbox;

/** the Operator Interface setup */
public class OI {
	
	public static final Joystick DRIVER = new Joystick(0);
	public static final Joystick OPERATOR = new Joystick(1);
	
	/** assigns what every SmartDash and controller button does */
	public OI() {
		/// manipulator wheels
			Button spinIn = new JoystickButton(OPERATOR, Xbox.RB);
					spinIn.whileHeld(new ButtonCommandEat());
			Button spinOut = new JoystickButton(OPERATOR, Xbox.LB);
					spinOut.whileHeld(new ButtonCommandSpit());
		/// manipulator clamp
			Button toggleClamp = new JoystickButton(OPERATOR, Xbox.A);
				toggleClamp.toggleWhenActive(new ToggleCommandClamp());
		/// candy cane
			Button toggleHook = new JoystickButton(OPERATOR, Xbox.B);
				toggleHook.toggleWhenActive(new ToggleCommandHook());
		/// drop the mast
			Button dropIt = new JoystickButton(OPERATOR, Xbox.X);
				dropIt.toggleWhenPressed(new ButtonCommandHitTheDeck());
		/// Reversing mode
			Button toggleReverse = new JoystickButton(DRIVER, Xbox.Y);
				toggleReverse.toggleWhenPressed(new ToggleCommandReverse());
		/// Docking mode
			Button toggleDock = new JoystickButton(DRIVER, Xbox.X);
				toggleDock.toggleWhenPressed(new ToggleCommandDock());
		/// To Compress, or Not To Compress. It is now an option.
			SmartDashboard.putData("Disable Compressor", new ToggleCommandKillCompressor());
			

		/// PID
			SmartDashboard.putData("Kill PID", new ToggleCommandKillPID());
			SmartDashboard.putNumber("Right Encoder Position", 0);
			SmartDashboard.putNumber("Left Encoder Position", 0);
			
		/// limit switch displays
			SmartDashboard.putBoolean("Lower Screw", true);
	    	SmartDashboard.putBoolean("Upper Screw", false);
	    	SmartDashboard.putBoolean("Lower Pinion", true);
	    	SmartDashboard.putBoolean("Upper Pinion", false);
	    	
	    	SmartDashboard.putNumber("Left inches", 0);
	    	SmartDashboard.putNumber("Right inches", 0);
	    	
	    	DriverStation.reportWarning("OI IS INSTANTIATED", false);

	    /// Cyborg command testers
			SmartDashboard.putData("Drive Direct", new CyborgCommandDriveDirect(Util.getAndSetDouble("Drive Direct Power", 0)));
			SmartDashboard.putData("Drive Distance", new CyborgCommandDriveDistance(Util.getAndSetDouble("Drive Distance Inches", 0)));
			SmartDashboard.putData("Drive Until Error", new CyborgCommandDriveUntilError());
			SmartDashboard.putData("Rotate Degree", new CyborgCommandRotateDegrees(Util.getAndSetDouble("Rotate Degrees", 0)));
			SmartDashboard.putData("Spit", new CyborgCommandSpit((long)Util.getAndSetDouble("Spit Time", 500)));
			SmartDashboard.putData("Raise to Position: Pinion Up", new CyborgCommandGrow(Mast.PINION_UP));
			SmartDashboard.putData("Raise to Position: Pinion Down", new CyborgCommandGrow(Mast.PINION_DOWN));
			SmartDashboard.putData("Raise to Position: Screw Up", new CyborgCommandGrow(Mast.SCREW_UP));
			SmartDashboard.putData("Raise to Position: Screw Down", new CyborgCommandGrow(Mast.SCREW_DOWN));
	}
	
}