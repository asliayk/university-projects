l = length(x); % number of samples
ff = fft(x,l); % computes DFT of x using fast fourier transform
x0 = fftshift(abs(ff)); % shift values
f0 = (-l/2:l/2-1)*(fs/l); % 0-centered frequency range
plot(f0,x0); % frequency values (1), which are 25, 65 and 120
x_phase = angle(x0);
%plot(f0, x_phase/pi);
% phase values (2), which are all 0
%plot(abs(ff)/(l/2));
% amplitude values (3), which are all 1.7