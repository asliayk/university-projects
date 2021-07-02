code segment
mov cx, 0
mymain:		
	mov ah, 01h				; program reads character
	int 21h
	mov ah, 0
	cmp al, 0Dh				; program jumps to the first phase of the print part when the input finishes, when entered carriage return
	je start_to_print
	cmp al, 20h             ; program starts combining the characters from the stack when it sees a space
	je to_handle_stack	
	cmp al, 2Bh				; program jumps to the addition part when it reads the plus sign
	je add_jump	
	cmp al, 2Ah				; program jumps to the multiplication part when it reads the multiplication sign 
	je mult_jump
	cmp al, 2Fh 			; program jumps to the division part when it reads the division sign
	je div_jump
	cmp al, 26h				; program jumps to the and case part when it reads the bitwise and sign
	je and_jump
	cmp al, 7Ch				; program jumps to the or case part when it reads the bitwise or sign
	je or_jump
	cmp al, 5Eh				; program jumps to the xor case part when it reads the bitwise xor sign
	je xor_jump
	cmp al, 39h				; program decides whether the input character is a digit or a letter by looking its ascii value,
                            ; and jumps to the right part to turn it into its hexadecimal value 
	jle gohandledigit
	ja gohandleletter
start_to_print:				; when user enters carriage return, program starts printing
	mov bh, 0				; digit counter
	mov cx, 16d				; set for the division by 16
	pop ax
	jmp divide_for_print
divide_for_print:       	; while loop/it separates the final result into characters
	mov dx, 0
	div cx
	inc bh					; increments the digit counter of the output result
	push dx
	cmp ax, 0				; program decides whether the output is ready to print or not and jumps to the right part
	jz final_print
	jmp divide_for_print
final_print:				; begin while-loop / program prints each character in the stack onto screen
	cmp bh, 0				; when digit counter reaches zero, program terminates
	je terminateprogram
	mov cx, 0
	pop cx
	cmp cx, 9h				; program decides whether the character is a digit or a letter, and jumps to the right part to turn it into its ascii value		
	jle handle_ascii_number
	ja handle_ascii_letter
handle_ascii_number:		; program turns each number character into their ascii values
	add cx, 30h
	mov dx, cx
	dec bh					; decrements the digit counter of the output result
	mov ah, 02h				; print character
	int 21h
	jmp final_print			; back to while-loop
handle_ascii_letter:		; program turns each letter character into their ascii values
	add cx, 37h
	mov dx, cx
	dec bh					; decrements the digit counter of the output result
	mov ah, 02h				; print character
	int 21h
	jmp final_print			; back to while-loop
to_handle_stack:
	jmp handle_stack
terminateprogram:			; terminates the program
	int 20h
gohandledigit:				; intermediate jump call
	jmp handle_digits
gohandleletter:				; intermediate jump call
	jmp handle_letter
add_jump:					; intermediate jump call
	jmp add_case
mult_jump:					; intermediate jump call
	jmp mult_case
div_jump:					; intermediate jump call
	jmp div_case
and_jump:					; intermediate jump call
	jmp and_case
or_jump:					; intermediate jump call
	jmp or_case
xor_jump:					; intermediate jump call
	jmp xor_case
handle_stack:				; looks for the input size to combine the characters
	cmp cl, 0				; 0 digit case
	je returnmain
	mov bx, 0
	cmp cl, 1				; jumps to 1 digit case
        je lab0
	cmp cl, 2				; jumps to 2 digit case
        je lab1
	cmp cl, 3				; jumps to 3 digit case
        je lab2
	cmp cl, 4				; jumps to 4 digit case
        je lab3
	mov cl, 0
	jmp returnmain
lab3:						; program first evaluates the least significant digit so it jumps up to lab2
	jmp lab2
lab3e: 						; if the number has 4 digits, it evaluates the hexadecimal value of the current leftmost digit
	pop ax
	shl ax, 12				; multiplies the digit by 16-cubed
	add bx, ax
	push bx
	mov cl, 0				; if the number is completely evaluated, digit counter should be 0 and wait for the new number
	jmp mymain
lab2:						; program first evaluates the least significant digit so it jumps up to lab1
	jmp lab1				
lab2e:						; if the number has 3 digits, it evaluates the hexadecimal value of the current leftmost digit
	pop ax
	mov ch, 16d
	mul ch
	mov ch, 16d				; multiplies the digit by 16-square
	mul ch
	add bx, ax
	cmp cl, 3				; if the element consists of more than three digits, the program continues to evaluate it
	jne lab3e				; jumps to 4-digit case
	push bx
	mov cl, 0				; if the number is completely evaluated, digit counter should be 0 and wait for the new number
	jmp mymain
returnmain:					; intermediate jump call
	jmp mymain
lab1:						; program first evaluates the least significant digit so it jumps up to lab0
	jmp lab0
lab1e:        				; if the number has 2 digits, it evaluates the hexadecimal value of the current leftmost digit
	pop ax
	mov ch, 16d				; multiplies the digit by 16
	mul ch
	add bx, ax
	cmp cl, 2				; if the element consists of more than two digits, the program continues to evaluate it
	jne lab2e				; jumps to 3-digit case
	push bx
	mov cl, 0				; if the number is completely evaluated, digit counter should be 0 and wait for the new number
	jmp mymain
lab0:						; here program evaluates the least significant digit
	mov bx,0
	pop ax
	mov ch, 1
	mul ch					; multiplies the digit by 1
	add bx, ax
	cmp cl, 1				; if the element consists of more than one digit, the program continues to evaluate it
	jne lab1e
	push bx
	mov cl, 0				; if the number is completely evaluated, digit counter should be 0 and wait for the new number
	jmp mymain
handle_digits:				; program turns the ascii value of a digit into its hexadecimal value and puts to the stack
	sub al, 48d
	push ax
	inc cl					;increments the digit counter of an element
	jmp returnmain
handle_letter:				; program turns the ascii value of a letter into its hexadecimal value and puts to the stack
	sub al, 37h
	push ax
	inc cl					;increment of the digit counter of an element
	jmp returnmain
add_case:               	; program makes the addition of two elements
	pop ax
	pop bx
	add ax, bx
	push ax
	jmp mymain
mult_case:					; program makes the multiplication of two elements	
	pop bx
	pop ax
	mul bx
	push ax
	jmp mymain
div_case:              		; program makes the division case of two elements
	mov dx, 0
	pop bx
	pop ax
	div bx
	push ax
	jmp mymain
and_case:					; program makes the and case of two elements
	pop ax
	pop bx
	and ax, bx
	push ax
	jmp mymain
or_case:					; program makes the or case of two elements
	pop ax
	pop bx
	or ax, bx
	push ax
	jmp mymain
xor_case:					; program makes the xor case of two elements
	pop ax
	pop bx
	xor ax, bx
	push ax
	jmp mymain
code ends
