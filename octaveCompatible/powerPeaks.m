function [pklg,lclg,minPeakHeight] = powerPeaks(arr1, shortPara, heightPara)

	[pksh,lcsh] = findpeaks(arr1,'MinPeakHeight',0); % default meanpeakheight is 2*std (abs (detrend (arr1,0)))

	lcsh1 = lcsh;
	lcsh = sort(lcsh); % so that short doesnt become negative.

	if length(lcsh) > 1
		short = mean(diff(lcsh));
	else
		if length(lcsh)>0
			short = lcsh(1)-1;
		else
			short=1;
		end
	end

	short = short * shortPara;
	minPeakHeight = mean(arr1)*heightPara;
	%minPeakHeight = mean(arr1)-std(arr1);

	[pklg,lclg] = findpeaks(arr1, 'MinPeakDistance',ceil(short),'MinPeakHeight',ceil(minPeakHeight));

	% The following draws the energy plot and plots the outputs of both the findpeaks.
	%plot(arr1);
	%hold on;
	%plot( lcsh1 , pksh, 'k^','markerfacecolor','blue');
	%hold on;
	%plot( lclg , pklg, 'k^','markerfacecolor','red');
	%hold off;

end;