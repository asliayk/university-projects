t = (-2:0.01:2);
% t and y1,y2..,y9 are given in the question
y1 = sin(2*pi.*t);
y2 = sin(2*pi*10.*t);
y3 = 10*sin(2*pi.*t);
y4 = sin(2*pi.*t)+10;
y5 = sin(2*pi.*(t-0.5));
y6 = 10*sin(2*pi*10.*t);
y7 = sin(2*pi.*t).*t;
y8 = sin(2*pi.*t)./t;
y9 = y1+y2+y3+y4+y5+y6+y7+y8;


subplot(5,2,1), plot(t, y1), xlim([-2 2]), title('y1')
subplot(5,2,2), plot(t, y2), xlim([-2 2]), title('y2')
subplot(5,2,3), plot(t, y3), xlim([-2 2]), title('y3')
subplot(5,2,4), plot(t, y4), xlim([-2 2]), title('y4')
subplot(5,2,5), plot(t, y5), xlim([-2 2]), title('y5')
subplot(5,2,6), plot(t, y6), xlim([-2 2]), title('y6')
subplot(5,2,7), plot(t, y7), xlim([-2 2]), title('y7')
subplot(5,2,8), plot(t, y8), xlim([-2 2]), title('y8')
subplot(5,2,9), plot(t, y9), xlim([-2 2]), title('y9')