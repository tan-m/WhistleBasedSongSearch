function [] = getSur(path)
tStart = tic;
[x, fs] = wavread(path);
[pitch, energy] = calculatePitchEnergy(x, fs);
[trough_before, trough_after, width, peakLoc] = calculateSliceBoundaries(pitch,energy);
[finalPitch] = calculateFinalPitch(pitch,energy,trough_before, trough_after, width, peakLoc);
finalPitch
disp('Time ');
tElapsed = toc(tStart)