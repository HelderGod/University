.data

	ImageA: 	.asciz  "/home/helder/Desktop/work.rgb"		#rgb image
	ImageB:		.asciz 	"/home/helder/Desktop/work.gray"	#gray image
	BUFFER_A:	.space 	786432 					#Buffer rgb
	BUFFER_B:	.space 	262144					#Buffer gray
	
	
.text

#	a0-	ficheiro a ser lido
#	a1- 	endereço onde vai ser escrita
#	
#
						
read_rgb_image:
	
	la a0, ImageA
	li a7, 1024		#open file
	li a1, 0
	ecall
	mv s2, a0
	
	li a7, 63		#read file
	mv a0, s2	
	la a1, BUFFER_A
	li a2, 786432
	ecall
	mv s3, a0
	
	li a7, 57		#close file		
	mv a0, s2
	ecall
	mv a0, s3
	
	ret
	
	
	
#	a0- ficheiro a ser escrito
#	a1- buffer da imagem
#	a2 - comprimento
#
	
write_gray_image:
	
	la a0, ImageB
	li a7, 1024
	li a1, 1
	ecall
	mv s1, a0
	
	li a7, 64
	mv a0, s1
	la a1, BUFFER_B
	li a2, 262144
	ecall
	
	li a7, 57
	mv a0, s1
	ecall
	ret
	
	
	
