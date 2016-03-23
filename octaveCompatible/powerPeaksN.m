function [pklg,lclg,minPeakHeight] = powerPeaksN(arr1, shortPara, heightPara)

%arr2 = zeros(length(arr1));
%for i=1:length(arr1)
%	arr2(i) = arr1(i);
arr2 = arr1;
arr1 = 1.01*max(arr1) - arr1;



[pksh,lcsh] = findpeaks(arr1,'MinPeakHeight',0);
lcsh1 = lcsh;
lcsh = sort(lcsh);

if length(lcsh) > 1
short = mean(diff(lcsh));
%disp('--------');


else
    if length(lcsh)>0
        short = lcsh(1)-1;
    else
        short=1; %change
    end
end

short = short * shortPara
minPeakHeight = mean(arr1)*heightPara
%minPeakHeight = mean(arr1)-std(arr1);

%disp(short);
%disp('--------');

[pklg,lclg] = findpeaks(arr1,'MinPeakDistance',ceil(short),'MinPeakheight',ceil(minPeakHeight))

%[pklg,lclg] = findpeaks(arr1,'MinPeakDistance',ceil(short))

plot(arr1);
hold on;
%plot(arr2);
%hold on;
plot( lcsh1 , pksh, 'k^','markerfacecolor',[0 0 1]);
%hold on;
%plot( lclg , pklg, 'k^','markerfacecolor',[1 0 0]);
hold off;
end