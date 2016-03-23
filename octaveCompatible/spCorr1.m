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
%	And changes made by WhizSearch team
% USES
%   xcorr.m (Signal Processing toolbox)

function [p_sh, p_lg] = spCorr(x, fs, maxlag, show)

	 r = xcorr(x,'coeff');
	 
	 r_slice = r(ceil(length(r)/2) : length(r));
	 r_slice = r_slice + 1; % done so as to make the input signal positive
	 

	[pksh,lcsh] = findpeaks(r_slice, 'MinPeakHeight',0);

% 	clf;
% 	plot(r_slice);
% 	hold on;
% 	plot(lcsh,pksh,'k^','markerfacecolor','blue');
% 	hold on;

	if length(lcsh) > 1
		lcsh = sort(lcsh); % octave doesnt give time ordered list and hence 'short' becomes negative. Hence sorted. 
		d = diff(lcsh);
		short = mean(d)*1; %+ std(d)/2;
% 		fprintf('mean is'); disp(mean(d));
% 		fprintf('std is'); disp(std(d));
	else
		if length(lcsh)>0
			short = lcsh(1)-1;
		else
			short=1;
		end
	end

	[pklg,lclg] = findpeaks(r_slice, 'MinPeakDistance', ceil(short),'MinPeakheight',1.3);
	
	if length(lclg) > 1
	lclg = sort(lclg);
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
		%plot(lclg, pklg, 'k^','markerfacecolor','red');
		%disp('--------');
		%disp(long);
		%disp(p_lg);
		%hold off;
	end
end  