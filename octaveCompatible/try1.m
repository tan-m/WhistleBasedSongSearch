%find all peaks significantly above noise floor
function [pitch] =  try1()
[pks,locs] = findpeaks(yData,'SortStr','descend','MinPeakHeight',1.3);
xLocs = xData(locs);           

%find number of peak clusters
numPeaks = length(unique(round(xLocs/100)));

%find locations of numPeaks peaks
[pks,locs] = findpeaks(yData,'SortStr','descend','npeaks',numPeaks);