	.data
a:
	70
	80
	40
	20
	10
	30
	50
	60	
n:
	8	
	.text
main:
	load %x0, $n, %x3
	subi %x3, 1, %x4
	addi %x0, 0, %x5
	load %x4, $a, %x6
	subi %x0, 1, %x11
	jmp loop
loop:
	load %x5, $a, %x7
	bgt %x7, %x6, switch
	store %x7, 0, %x4
	store %x6, 0, %x5
	load %x4, $a, %x6
	addi %x5, 1, %x5
	beq %x5, %x3, refresh
	beq %x0, %x0, loop
switch:
	addi %x5, 1, %x5
	beq %x5, %x3, refresh
	beq %x0, %x0, loop
refresh:
	addi %x0, 0, %x5	
	subi %x4, 1, %x4
	subi %x3, 1, %x3
	blt %x4, %x0, exit
	load %x4, $a, %x6
	bgt %x4, %x11, loop
	end	
exit:
	end	
