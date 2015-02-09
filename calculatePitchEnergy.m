function [pitchFile, energyFile] = calculatePitchEnergy(x, fs)
w = 1000; % window size
n = length(x); % no of samples in wav file
factor = 2; % factor for sliding
noOfPitches = floor(floor(n - w) / floor(w - floor(w/factor)) ) + 1;

start =  1;
stop = w;
energy = zeros(1,noOfPitches);
pitch = zeros(1,noOfPitches);
cnt=1;
while stop < floor(length(x))
    x1 = x(start:stop);
    [p_sh, p_lg] = spCorr1(x1, fs, [], '%plot');
    if p_lg > 0
        pitch(cnt) = cast(p_lg,'uint32');
        energy(cnt) = sum(x1.^2);
        cnt = cnt + 1;
    else
        %do nothing
    end
    start = start+ (w - (w/factor));    
    stop = stop + (w - (w/factor));
end
pitchFile = pitch(1:cnt-1);
energyFile = energy(1:cnt-1);