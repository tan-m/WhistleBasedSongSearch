function [] = helper()
	load myfile.mat;
	%[trough, troughLoc,minPeakHeight1]= powerPeaksN(energySmooth, 0.3, 0.8);
	[trough, troughLoc,minPeakHeight1]= powerPeaks(energySmooth, 0.7, 0.6);
end