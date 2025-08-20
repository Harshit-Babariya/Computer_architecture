	.data
a:
	10
	.text
main:
	load %x0, $a, %x3
	addi %x0, 2, %x4
	addi %x0, 1, %x6
	bgt %x3, %x6, loop
	subi %x0, 1, %x10
	end
loop:
	beq %x4, %x3, done
	div %x3, %x4, %x5
	beq %x31, %x0, nonprime
	addi %x4, 1, %x4
	beq %x0, %x0, loop
done:
	addi %x0, 1, %x10
	end
nonprime:
	subi %x0, 1, %x10
	end
