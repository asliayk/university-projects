#lang scheme


(define (TRANSPORTATION-COST farm)
  (rec-cost farm FARMS))

(define (rec-cost farm list)
  (cond
   ((null? list) 0)
   ((equal? farm (list-ref (car list) 0)) (list-ref (car list) 1))
   (else (rec-cost farm (cdr list)))
   ))

(define (AVAILABLE-CROPS farm)
  (rec-acrop farm FARMS))

(define (rec-acrop farm list)
  (cond
   ((null? list) '())
   ((equal? farm (list-ref (car list) 0)) (list-ref (car list) 2))
   (else (rec-acrop farm (cdr list)))
   ))

(define (INTERESTED-CROPS customer)
  (rec-icrop customer CUSTOMERS))

(define (rec-icrop customer list)
  (cond
   ((null? list) '())
   ((equal? customer (list-ref (car list) 0)) (list-ref (car list) 2))
   (else (rec-icrop customer (cdr list)))
   ))

(define (CONTRACT-FARMS customer)
  (rec-ccrop customer CUSTOMERS))

(define (rec-ccrop customer list)
  (cond
   ((null? list) '())
   ((equal? customer (list-ref (car list) 0)) (list-ref (car list) 1))
   (else (rec-ccrop customer (cdr list)))
   ))

(define (CONTRACT-WITH-FARM farm)
  (rec-cwfcrop farm CUSTOMERS ))

(define (member a list)
  (cond
    ((null? list) #f)
    ((equal? a (car list)) #t)
    (else (member a (cdr list)))
))    
    
(define (rec-cwfcrop farm list)
  (cond
   ((null? list) '())
   ((member farm (list-ref (car list) 1)) (cons (list-ref (car list) 0) (rec-cwfcrop farm (cdr list)))) 
   (else (rec-cwfcrop farm (cdr list)))
   ))

(define (INTERESTED-IN-CROP crop)
  (rec-iiccrop crop CUSTOMERS ))

(define (rec-iiccrop crop list)
  (cond
   ((null? list) '())
   ((member crop (list-ref (car list) 2)) (cons (list-ref (car list) 0) (rec-iiccrop crop (cdr list)))) 
   (else (rec-iiccrop crop (cdr list)))
   ))

(define (crop-price crop)
  (sort (rec-cropprice crop CROPS) <))

(define (rec-cropprice crop list)
  (cond
   ((null? list) '())
   ((equal? crop (list-ref (car list) 0)) (cons (list-ref (car list) 2) (rec-cropprice crop (cdr list)))) 
   (else (rec-cropprice crop (cdr list)))
   ))

(define (MIN-SALE-PRICE crop)
  (cond
    ((null? (crop-price crop)) 0)
    (else (car (crop-price crop))
   )))       
  
(define (CROPS-BETWEEN min max)
  (rec-cropbetween min max CROPS ))

(define (append a list)
  (cond
    ((null? list) a)
    (else (cons(car list) (append (cdr list) a)))
))

(define (rec-cropbetween min max list)
  (cond
   ((null? list) '())
   ((and (>= max (list-ref (car list) 2)) (<= min (list-ref (car list) 2)) (not(member (list-ref (car list) 0) (rec-cropbetween min max (cdr list)))))  (cons (list-ref (car list) 0) (rec-cropbetween min max (cdr list))))
         (else (rec-cropbetween min max (cdr list)))
   ))

(define (crop-list crop)
  (rec-croplist crop CROPS ))

(define (rec-croplist crop list)
  (cond
    ((null? list) '())
    ((equal? crop (list-ref (car list) 0)) (cons (car list) (rec-croplist crop (cdr list)))) 
   (else (rec-croplist crop (cdr list)))
   )) 

(define (BUY-PRICE customer crop)
  (cond
    ((or (null? (INTERESTED-CROPS customer)) (null? (INTERESTED-IN-CROP crop))) '0)
    (else
  (let ((farmlist (CONTRACT-FARMS customer)) (croplist (crop-list crop)))
   (car (sort (rec-buyprice farmlist crop croplist) <))))
    ))

(define (rec-buyprice farmlist crop croplist)
  (cond
    ((null? croplist) '())
    ((member (list-ref (car croplist) 1) farmlist) (cons (+ (list-ref (car croplist) 2) (TRANSPORTATION-COST (list-ref (car croplist) 1))) (rec-buyprice farmlist crop (cdr croplist))))
    (else (rec-buyprice farmlist crop (cdr croplist)))
    ))

(define (TOTAL-PRICE customer)
  (let ((croplist (INTERESTED-CROPS customer)))
  (sum (rec-totprice customer croplist))))

(define (rec-totprice customer croplist)
  (cond
    ((null? croplist) '())
    (else (cons (BUY-PRICE customer (car croplist)) (rec-totprice customer (cdr croplist))))
    ))

(define (sum list)
  (cond
    ((null? list) 0)
    (else (+ (car list) (sum (cdr list)))
  )))



  
  
