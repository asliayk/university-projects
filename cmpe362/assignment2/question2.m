[y,fs] = audioread('.\faultyphone.wav'); % sample data and sample rate

len = length(y); % number of samples
ff = fft(y,len); % computes DFT of x using fast fourier transform
x1 = fftshift(abs(ff)); % shift the magnitude of fft values
f0 = (-len/2:len/2-1)*(fs/len); % 0-centered frequency range

plot(f0,x1);
[maxvalues] = find(x1==max(x1));  % finding the index of la sound in frequency domain

filter = ones(len,1);
max1 = maxvalues(1,1);
max2 = maxvalues(2,1);

% making the necessary indexes of filter equal to 0 to ignore the la sound
% frequency
filter(max1,1) = 0;
filter(max2,1) = 0;

% filter value in time domain
fresult = ifft(ifftshift(filter)) ;

% convolution operation
convval = cconv(fresult,y, len);

len2 = length(convval); % number of samples
ff2 = fft(convval,len); % computes DFT of x using fast fourier transform
x2 = fftshift(abs(ff2)); % shift the magnitude of fft values
f0 = (-len2/2:len2/2-1)*(fs/len2); % 0-centered frequency range

% plotting frequency after filtering
%plot(f0,x2);

% listening filtered sound in matlab
soundsc(convval,fs);

% creating sound file
filename = 'filtered.wav';
audiowrite(filename,convval,fs);
