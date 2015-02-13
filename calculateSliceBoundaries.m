function [trough_before, trough_after, width, peakLoc] = calculateSliceBoundaries(pitchFile, energyFile)

% first smooth the energy curve
g = gausswin(20); % <-- this value determines the width of the smoothing window
g = g/sum(g);
energySmooth = conv(energyFile, g, 'same');


[peak, peakLoc] = powerPeaks(energySmooth,0.7, 0.8);

[trough, troughLoc]= powerPeaks(-energySmooth, 0.5, 3);

% plot(energySmooth,'Color','blue'); hold on;
%
%   plot(peakLoc,energySmooth(peakLoc),'k^','markerfacecolor',[1 1 0]);
%   plot(troughLoc,energySmooth(troughLoc),'k^','markerfacecolor',[1 0 0]);
%  hold off;

width = zeros(size(peak));
trough_before = zeros(size(peak));
trough_after = zeros(size(peak));
for ii = 1:numel(peak)
    
    temp1= troughLoc(...
        find(troughLoc < peakLoc(ii), 1,'last') );
    if(isempty(temp1))
        temp1 = 1;  % for first peak there if there is no trough before it
    end
    trough_before(ii)=temp1;
    
    temp2  = troughLoc(...
        find(troughLoc > peakLoc(ii), 1,'first') );
    
    if(isempty(temp2))
        temp2 = length(energySmooth); % for last peak if there is no trough after it
    end
    trough_after(ii)=temp2; 
    
    
    width(ii) = trough_after(ii) - trough_before(ii);
    
end
