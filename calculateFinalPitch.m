function [finalPitch] = calculateFinalPitch(pitchFile,energyFile,trough_before, trough_after, width, peakLoc)
before = zeros(size(peakLoc));
after = zeros(size(peakLoc));
mode2 = zeros(size(peakLoc));
finalPitch = zeros(size(peakLoc));
mode3array = zeros(size(peakLoc));

for i = 1 : length(trough_after)
    w = floor(width(i)/5);
    before(i) = peakLoc(i) - w;
    if before(i) < trough_before(i)
        before(i) = trough_before(i);
    end
    
    
    after(i) = peakLoc(i) + w;
    if after(i) > trough_after(i)
        after(i) = trough_after(i);
    end
    
    slice= pitchFile(before(i):after(i));
    sliceSmooth = medfilt1(double(slice),7); % median filter 
    
    
    
    %disp('a slice');
    %disp(pitchFile(trough_before(i):trough_after(i)));
    
     
    median2 = median(sliceSmooth);
    mode2(i) = mode(sliceSmooth);
    
    
    % if standard deviation is too big make it one fourth, then if it is
    % too small make it 12
    std2 = std(sliceSmooth);
    if(std2 > 12)
        std2 = std2/4;
        if(std2 < 12)
            std2 = 12;
        end
    end
    low = (mode2(i)+median2)/2 - std2;
    high = (mode2(i)+median2)/2 + std2;
    
    % low should be minimum among low, median2, mode2
    % high should be maximum among high, median2, mode2
    
    stat(1) = low;
    stat(2) = mode2(i);
    stat(3) = median2;
    low = min(stat);
    
    stat(1) = high;
    high = max(high);
    
    fullSlice= pitchFile(trough_before(i):trough_after(i));
    filteredSlice = fullSlice(fullSlice>low & fullSlice<high);
    
    % try to fit a line 
    p = polyfit(double(1:length(filteredSlice)),double(filteredSlice),1);
    
    
    mode3 = mode(filteredSlice); %mode of filtered slice
    % if line has near zero slope pick its y intercept else pick the mode
    if(p(1)<0.2 & p(1)>-0.2)
        finalPitch(i) = uint32((p(2)));
    else
        finalPitch(i) = uint32(mode3);
    end
    mode3array(i)=mode3;
    %plot(before(i):after(i),sliceSmooth,'Color','red');
    % %plot(before(i):after(i), p(1)*(before(i):after(i))+p(2),'Color','black');
end


% plot(1:length(pitchFile),mode2,'k^','markerfacecolor',[0 1 1]);

%plot(before,multFactor*energySmooth(before),'k^','markerfacecolor',[1 1 0]);
%plot(after,multFactor*energySmooth(after),'k^','markerfacecolor',[1 0 0]);



%hold off;
