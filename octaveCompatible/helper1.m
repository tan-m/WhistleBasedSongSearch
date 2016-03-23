function [] = helper1(path)
	 [x, fs, foo] = wavread(path);
	 w = 1000; % window size
	start =  1;
	stop = w;
	cnt = 1;
	disp(fs);
	while stop < floor(length(x))
    x1 = x(start:stop);
	fprintf('%d.',cnt);
	disp(sum(x1));
    start = start+w;
    stop = stop + w;
	cnt = cnt + 1;
	end
end