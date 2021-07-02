RGB = imread('monarch.png'); % reading the image
I = rgb2gray(RGB); % converting the rgb image into a grayscale image
minVal = min(I(:)); % minimum value of image matrix
[rowMin,colMin] = find(I==minVal); % location of the minimum value of the matrix
maxVal = max(I(:)); % maximum value of the matrix
[rowMax,colMax] = find(I==maxVal); % location of the maximum value of the matrix
meanVal = mean(I(:)); % mean value of the matrix
stdVal = std(double(I(:))); % standard deviation value of the matrix