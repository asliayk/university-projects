% ÖNCE Q1 Ý ÇALIÞTIR
z = randn(1, 401); 
z = 0.1.*z;
% numbers are multiplied by 0.1
t = (-2:0.01:2); % t vector is given
y10 = z;
% y10,y,11….,y19 are given
y11 = z+t;
y12 = z+y1;
y13 = z.*y1;
y14 = sin(2*pi.*z).*t;
y15 = sin(2*pi.*(t+z));
y16 = z.*y2;
y17 = sin(2*pi.*(t+10.*z));
y18 = y1./z;
y19 = y11+y12+y12+y14+y15+y16+y17+y18;

subplot(5,2,1), plot(y10), xlim([0 401]), title('y10');
subplot(5,2,2), plot(t, y11), xlim([-2 2]), title('y11');
subplot(5,2,3), plot(t, y12), xlim([-2 2]), title('y12');
subplot(5,2,4), plot(t, y13), xlim([-2 2]), title('y13');
subplot(5,2,5), plot(t, y14), xlim([-2 2]), title('y14');
subplot(5,2,6), plot(t, y15), xlim([-2 2]), title('y15');
subplot(5,2,7), plot(t, y16), xlim([-2 2]), title('y16');
subplot(5,2,8), plot(t, y17), xlim([-2 2]), title('y17');
subplot(5,2,9), plot(t, y18), xlim([-2 2]), title('y18');
subplot(5,2,10), plot(t, y19), xlim([-2 2]), title('y19');