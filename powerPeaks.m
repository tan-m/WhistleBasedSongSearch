function [pklg,lclg,minPeakHeight] = powerPeaks(arr1, shortPara, heightPara)


[pksh,lcsh] = findpeaks(arr1);

if length(lcsh) > 1
short = mean(diff(lcsh));
else
    if length(lcsh)>0
        short = lcsh(1)-1;
    else
        short=1; %change
    end
end

short = short * shortPara;
minPeakHeight = mean(arr1)*heightPara;
%minPeakHeight = mean(arr1)-std(arr1);


[pklg,lclg] = findpeaks(arr1, ...
    'MinPeakDistance',ceil(short),'MinPeakheight',ceil(minPeakHeight));
