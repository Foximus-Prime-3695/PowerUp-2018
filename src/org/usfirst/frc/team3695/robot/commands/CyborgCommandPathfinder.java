package org.usfirst.frc.team3695.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;
import org.usfirst.frc.team3695.robot.Robot;
import org.usfirst.frc.team3695.robot.subsystems.SubsystemDrive;

public class CyborgCommandPathfinder extends Command {

    private Waypoint[] waypoints;
    private TankModifier tankMod;

    private EncoderFollower leftEncoder;
    private EncoderFollower rightEncoder;

    //Good old PID values. Do not add I. Just don't
    private final static double P_LEFT = 0.0001;
    private final static double I_LEFT = 0.000;
    private final static double D_LEFT = 0.000;

    private final static double P_RIGHT = 0.0001;
    private final static double I_RIGHT = 0.000;
    private final static double D_RIGHT = 0.000;


    public CyborgCommandPathfinder(Waypoint[] points) {
        requires(Robot.SUB_DRIVE);
        waypoints = points;
    }

    public void initialize(){
        tankMod = Robot.SUB_DRIVE.autoDrive.generateTankMod(Robot.SUB_DRIVE.autoDrive.generateTrajectory(waypoints));

        leftEncoder = new EncoderFollower(tankMod.getLeftTrajectory());
        rightEncoder = new EncoderFollower(tankMod.getRightTrajectory());

        leftEncoder.configureEncoder(SubsystemDrive.leftMaster.getSelectedSensorPosition(0), 1000, SubsystemDrive.AutoDrive.WHEEL_DIAMETER);
        rightEncoder.configureEncoder(SubsystemDrive.rightMaster.getSelectedSensorPosition(0), 1000, SubsystemDrive.AutoDrive.WHEEL_DIAMETER);

        //TODO: add util getandsetdouble calls to all of the PID values so it isn't a mess to configure.
        //I'm just lazy right now and it looks all pretty without them
        leftEncoder.configurePIDVA(P_LEFT, I_LEFT, D_LEFT, 1/Robot.SUB_DRIVE.autoDrive.MAX_VELOCITY, Robot.SUB_DRIVE.autoDrive.ACC_GAIN);
        rightEncoder.configurePIDVA(P_RIGHT, I_RIGHT, D_RIGHT, 1/Robot.SUB_DRIVE.autoDrive.MAX_VELOCITY, Robot.SUB_DRIVE.autoDrive.ACC_GAIN);
        DriverStation.reportWarning("Pathfinder configuration complete", false);
    }

    protected void execute(){
        //Now that we've gotten setup for this drive, it's time to roll out!
        double leftOutput = leftEncoder.calculate((int)Robot.SUB_DRIVE.autoDrive.leftEncoderPos());
        double rightOutput = rightEncoder.calculate((int)Robot.SUB_DRIVE.autoDrive.rightEncoderPos());

        //All of this will account for any turning the robot makes when it drives
        //Way better than what we had with
        //Make sure gyro is in degrees!!!
        double gyroHeading = Robot.SUB_DRIVE.gyro.getAngle();
        //The sides of the robot are in parallel and therefore are always the same
        //So lets just use left
        double desiredHeading = Pathfinder.r2d(leftEncoder.getHeading());
        //The difference in angle we want to reach
        double angleDifference = Pathfinder.boundHalfDegrees(desiredHeading - gyroHeading);
        double turn = 0.8 * (-1.0/80.0) * angleDifference; //Blame Jaci. Not quite sure why this is what it is.

        Robot.SUB_DRIVE.autoDrive.setTalons(leftOutput + turn, rightOutput - turn);
    }

    protected boolean isFinished() {
        return leftEncoder.isFinished() && rightEncoder.isFinished();
    }
}
