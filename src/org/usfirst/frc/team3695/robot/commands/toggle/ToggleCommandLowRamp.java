package org.usfirst.frc.team3695.robot.commands.toggle;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team3695.robot.Robot;

/**
 * Toggle the bot in and out of docking mode
 *  Docking mode limits the maximum speed of the robot for more sensitive movements
 */
public class ToggleCommandLowRamp extends Command {

    private static final String smartDashKey = "Low Ramp";
    private static boolean enabled = false;
    private boolean isFinished;

    public ToggleCommandLowRamp() {
        requires(Robot.SUB_DRIVE);
        isFinished = false;
    }

    protected void initialize() { }

    protected void execute() {
        Robot.SUB_DRIVE.toggleLowRamp();
        SmartDashboard.putBoolean(smartDashKey, (enabled = !enabled));
        isFinished = true;
    }

    protected boolean isFinished() { return isFinished; }

    protected void end() {
        isFinished = false;
    }

    protected void interrupted() {
        end();
    }
}
