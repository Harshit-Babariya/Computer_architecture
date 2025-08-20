	.data
n:
	10
	.text
main:
	load %x0, $n, %x3
	addi %x0, 65535, %x4
	addi %x0, 0, %x5
	addi %x0, 1, %x6
	jmp loop
loop:
	beq %x3, %x0, exit
	store %x5, 0, %x4
	add %x5, %x6, %x7
	add %x6, %x0, %x5
	add %x7, %x0, %x6
	subi %x4, 1, %x4
	subi %x3, 1, %x3
	beq %x0, %x0, loop
exit:
	end
