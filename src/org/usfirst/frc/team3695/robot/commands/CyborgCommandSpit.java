package org.usfirst.frc.team3695.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team3695.robot.Robot;
import org.usfirst.frc.team3695.robot.util.Util;

public class CyborgCommandSpit extends Command {

	Boolean isFinished;
    long runTime;
    long startTime;
    
    public CyborgCommandSpit(long runTime) {
        requires(Robot.SUB_MANIPULATOR);
        this.runTime = runTime;
    }

    protected void initialize() {
        startTime = System.currentTimeMillis();
        runTime = (long) Util.getAndSetDouble("Spit Time", 500);
    }

    protected void execute() {
        if (startTime + runTime >= System.currentTimeMillis()){
            Robot.SUB_MANIPULATOR.spit();
        } else {
        	isFinished = true;
        }
    }

    protected boolean isFinished() { return isFinished; }

    protected void end() {
    	Robot.SUB_MANIPULATOR.stopSpinning();
    	isFinished = false;
    }

    protected void interrupted() {
        end();
    }
}
