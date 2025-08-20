	.data
a:
	10
	.text
main:
	load %x0, $a, %x3
	add %x3, %x0, %x6
	add %x0, %x0, %x4
	beq %x3, %x0, zero
	jmp loop
zero:
	addi %x0, 1, %x10
	end
loop:
	beq %x6, %x0, next
	divi %x6, 10, %x6
	addi %x31, 0, %x5
	muli %x4, 10, %x4
	add %x4, %x5, %x4
	beq %x0, %x0, loop
next:
	beq %x4, %x3, exit
	beq %x0, %x0, not
not:
	subi %x0, 1, %x10
	end
exit:
	addi %x0, 1, %x10
	end
