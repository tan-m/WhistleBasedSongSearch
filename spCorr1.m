% NAME
%   spCorr - Auto-correlation of a signal (Correlogram)
% SYNOPSIS
%   [r] = spCorr(x, fs, maxlag, show)
% DESCRIPTION
%   Obtain Auto-correlation coefficients of a signal
% INPUTS
%   x        (vector) of size Nx1 which contains signal
%   fs       (scalar) the sampling frequency
%   [maxlag] (scalar) seek the correlation sequence over the lag range 
%             [-maxlag:maxlag]. Output r has length 2*maxlag+1.
%             The default is 20ms lag, that is, 50Hz (the minimum possible
%             F0 frequency of human speech)
% OUTPUTS
%   r        (vector) of size 2*maxlag+1 which contains 
%             correlation coefficients
% AUTHOR
%   Naotoshi Seo, April 2008
% USES
%   xcorr.m (Signal Processing toolbox)
function [p_sh, p_lg] = spCorr(x, fs, maxlag, show)
 %% Initialization
%  if ~exist('maxlag', 'var') || isempty(maxlag)
%      maxlag = fs/50; % F0 is greater than 50Hz => 20ms maxlag
%  end
%  if ~exist('show', 'var') || isempty(show)
%      show = 0;
%  end
% disp('maxlag is ')
% disp(maxlag)
 %% Auto-correlation
 r = xcorr(x,'coeff');
 
 r_slice = r(ceil(length(r)/2) : length(r));
[pksh,lcsh] = findpeaks(r_slice);

if length(lcsh) > 1
short = mean(diff(lcsh));
else
    if length(lcsh)>0
        short = lcsh(1)-1;
    else
        short=1; %change
    end
end

[pklg,lclg] = findpeaks(r_slice, ...
    'MinPeakDistance',ceil(short),'MinPeakheight',0.3);
if length(lclg) > 1
long = mean(diff(lclg));
else
    if length(lclg) > 0
        long = lclg(1)-1;
    else
        long = -1;
    end
end

if long==-1
    p_sh=-1;
    p_lg=-1;
else
    p_sh = fs/short;
    p_lg = fs / long;
end
%  if show
%      %% plot waveform
%      t=(0:length(x)-1)/fs;        % times of sampling instants
%      subplot(2,1,1);
%      plot(t,x);
%      legend('Waveform');
%      xlabel('Time (s)');
%      ylabel('Amplitude');
%      xlim([t(1) t(end)]);
% 
%      %% plot autocorrelation
%      d=(-maxlag:maxlag)/fs;
%      subplot(2,1,2);
%      plot(d,r);
%      legend('Auto-correlation');
%      xlabel('Lag (s)');
%      ylabel('Correlation coef');
%  end