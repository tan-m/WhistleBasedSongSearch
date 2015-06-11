function [] = read(path)
tStart = tic;
[x, fs] = wavread(path);

w = 1000; % window size
n = length(x); % no of samples in wav file
factor = 2; % factor for sliding
noOfPitches = floor(floor(n - w) / floor(w - floor(w/factor)) ) + 1;

start =  1;
stop = w;
energy = zeros(1,noOfPitches);
pitch = zeros(1,noOfPitches);
%pitchFile = zeros(1,noOfPitches);
i=1;
cnt=1;
while stop < floor(length(x))
    x1 = x(start:stop);
    [p_sh, p_lg] = spCorr1(x1, fs, [], '%plot');
    energy(i) = sum(x1.^2);
    if p_lg > 0
        pitch(i) = p_lg;
        pitchFile(cnt) = cast(p_lg,'uint32');
        energyFile(cnt) = energy(i);
        cnt = cnt + 1;
        % %disp('pitch is '+pitch(i))
    else
        pitch(i) = 0;
    end
    start = start+ (w - (w/factor));
    stop = stop + (w - (w/factor));
    i = i + 1;
end
x1 = x(start:length(x));
[p_sh, p_lg] = spCorr1(x1, fs, [], '%plot');

arr = [1: i-1]; 
%%plot(arr,pitch);
%%plot(arr,energy);
arr1 = [1 : length(pitchFile)];
%%plot(arr1, pitchFile.*abs(pitchFile)/max(pitchFile));
%%plot(arr1,pitchFile);

g = gausswin(20); % <-- this value determines the width of the smoothing window
g = g/sum(g);
energySmooth = conv(energyFile, g, 'same');


[peak, peakLoc,minPeakHeight] = powerPeaks(energySmooth,0.7, 0.6);

[trough, troughLoc,minPeakHeight1]= powerPeaks(-energySmooth, 0.5, 3);



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
        temp1 = 1;
    end
    trough_before(ii)=temp1;
    
    temp2  = troughLoc(...
        find(troughLoc > peakLoc(ii), 1,'first') );
    
    if(isempty(temp2))
        temp2 = length(energySmooth);
    end
    trough_after(ii)=temp2;
    
    width(ii) = trough_after(ii) - trough_before(ii);
    
end

%disp('smooth f_slice');


multFactor = 5;
% plot(multFactor*energySmooth,'Color','blue'); hold on;
% plot(trough_before,multFactor*energySmooth(trough_before),'k^','markerfacecolor',[1 1 0]);
% plot(trough_after,multFactor*energySmooth(trough_after),'k^','markerfacecolor',[1 0 0]);
% 
% %plot(pitchFile,'Color','green');
% 
%  plot(1:length(energyFile), minPeakHeight, 'yellow');
%  
%  plot(1:length(energyFile), -minPeakHeight1, 'red');



% for i = 1 : length(trough_after)
%     i
%     %disp(pitchFile(trough_before(i):trough_after(i)));
%     %disp('mode');
%     %disp(mode(pitchFile(trough_before(i):trough_after(i))));
%     %disp('median ');
%     %disp(median(pitchFile(trough_before(i):trough_after(i))));
%     %disp(pitchFile(peakLoc(i)));
%
%     f_slice= pitchFile(trough_before(i):trough_after(i));
%     temp1 = medfilt1(double(f_slice),5.0);
%     temp2 = medfilt1(double(temp1),5.0);
%     f_sliceSmooth = medfilt1(double(temp2),5);
%
%     %disp('After smoothing');
%     %disp(f_sliceSmooth);
%     p = polyfit(trough_before(i):trough_after(i),f_sliceSmooth,1)
%     mode1 = mode(f_sliceSmooth)
%     median1 = median(f_sliceSmooth)
%     mean1 = mean(f_sliceSmooth)
%     %plot(trough_before(i):trough_after(i),f_sliceSmooth,'Color','red');
%
%     %plot(trough_before(i):trough_after(i), p(1)*(trough_before(i):trough_after(i))+p(2),'Color','black');
%     %disp('next slice ');
% end




before = zeros(size(peak));
after = zeros(size(peak));
mode2 = zeros(size(peak));
finalPitch = zeros(size(peak));
mode3array = zeros(size(peak));

fid1 = fopen('C:\Users\tanmay\Desktop\pitches_clusterDP.txt','wt');  % Note the 'wt' for writing in text mode
fid2 = fopen('C:\Users\tanmay\Desktop\energy_clusterDP.txt','wt');  % Note the 'wt' for writing in text mode

%fslice_count = 0;
%fprintf(fid2,'%f\n',fslice_count);  % The format string is applied to each element of a
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
    sliceSmooth = medfilt1(double(slice),7);
    
    
    
    %disp('a slice');
    %disp(pitchFile(trough_before(i):trough_after(i)));
    
    %mean2 = mean(sliceSmooth);
    median2 = median(sliceSmooth);
    mode2(i) = mode(sliceSmooth);
    
    
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
    
    
    %fprintf(fid1,'%f\n',filteredSlice);  % The format string is applied to each element of a
    fprintf(fid1,'%f\n',filteredSlice);  % The format string is applied to each element of a
    
    %fslice_count = fslice_count + length(filteredSlice);
    fprintf(fid2,'%f\n',mode(filteredSlice));  % The format string is applied to each element of a
    
    
    if length(filteredSlice) > 1
        p = polyfit(double(1:length(filteredSlice)),double(filteredSlice),1);


        mode3 = mode(filteredSlice); %mode of filtered slice
        if(p(1)<0.2 & p(1)>-0.2)
            finalPitch(i) = uint32((p(2)));
        else
            finalPitch(i) = uint32(mode3);
        end
        
         mode3array(i)=mode3;
    else
        %disp('Adding mode2. filteredSlice is empty');
        finalPitch(i) = mode2(i);
    end    
    
   
   
    %plot(before(i):after(i),sliceSmooth,'Color','red');
    
    % %plot(before(i):after(i), p(1)*(before(i):after(i))+p(2),'Color','black');
    
    
    
end

% finalPitch
% mode3array
% plot(1:length(pitchFile),mode2,'k^','markerfacecolor',[0 1 1]);

%plot(before,multFactor*energySmooth(before),'k^','markerfacecolor',[1 1 0]);
%plot(after,multFactor*energySmooth(after),'k^','markerfacecolor',[1 0 0]);



%hold off;

%y = medfilt1(double(pitchFile),5.0);


%noOfClusters = length(peakLoc);
% noOfClusters = 8;
% idx = kmeans(pitchFile,noOfClusters,'EmptyAction', 'singleton')

% %plot(pitchFile,'Color','blue');
%   hold on;
%   %plot(idx,'Color','blue');
%   hold off;

%peakfinder(energyFile)

fid = fopen('C:\Users\tanmay\Desktop\pitch.txt','wt');  % Note the 'wt' for writing in text mode
fprintf(fid,'%f\n',finalPitch);  % The format string is applied to each element of a
fclose(fid);
fclose(fid1);
fclose(fid2);
% disp('Matlab exec Time ')
%tElapsed = toc(tStart)