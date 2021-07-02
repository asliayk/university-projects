[y,fs] = audioread('.\enginesound.m4a'); % sample data and sample rate
size = length(y)/fs;  % time span signal
t = 0:1/fs:size-1/fs; % span time vector

avgsignal = y;

for index = 1:length(y)
    sum = 0;
    for aindex = 1:50  % summing 50 sample for each element in avg. filter
       if index+aindex<length(y)
          sum = sum + y(index+aindex); 
       else
          sum = sum + 0;
       end   
    end   
    sum = sum /50;   % sum of 50 samples should be divided by 5
    avgsignal(index)=sum;
end


%spectrogram(avgsignal);


subplot(2,1,1), plot(t, y), title('initial');
subplot(2,1,2), plot(t, avgsignal), title('50-point average');




