package processor.pipeline;

import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Operand;
import generic.Operand.OperandType;
import generic.Statistics;
import processor.Processor;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	EX_IF_LatchType EX_IF_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch, EX_IF_LatchType eX_IF_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	public void performOF()
	{
		//System.out.println("OF: "+IF_OF_Latch.getInstruction());
		if(!(OF_EX_Latch.getisprogramfinished()))
		{
			if (OF_EX_Latch.isexbusy()) {
				IF_OF_Latch.setislock(true);  
				//System.out.println("hi in of , isexbusy");
				return;
			}
			
			
			if((EX_IF_Latch.isEx_IF_enable()))
			{
				OF_EX_Latch.setinstruction(null);
				IF_EnableLatch.setislocked(false);
				IF_OF_Latch.setisprogramfinished(false);
				IF_OF_Latch.setislock(false);
			}
			else
			{
				//TODO
				if(!(IF_OF_Latch.getisinstructionpresent()))
				{
					OF_EX_Latch.setinstruction(null);
					IF_EnableLatch.setislocked(false);
					
				}
				else
				{
					Instruction instruction= new Instruction();
					int inst=IF_OF_Latch.getInstruction();
					if(IF_OF_Latch.getEXinstruction()!=null)
					{
						Instruction EX_instruction=IF_OF_Latch.getEXinstruction();
						int EX_opcode=EX_instruction.getOperationType().ordinal();
						if ((EX_opcode<23))
						{
							int EX_rd=EX_instruction.getDestinationOperand().getValue();							
							int Ofinstruction=inst;
							String OF_ansstring=String.format("%32s", Integer.toBinaryString(Ofinstruction)).replace(' ', '0');
							int OF_opcode=Integer.parseInt(OF_ansstring.substring(0,5),2);
							if(OF_opcode!=29)
							{
								if((OF_opcode%2==0 && OF_opcode<=21)||(OF_opcode>=25 && OF_opcode<=28)||(OF_opcode==23))
								{
									int OF_rs1=Integer.parseInt(OF_ansstring.substring(5,10),2);
									int OF_rs2=Integer.parseInt(OF_ansstring.substring(10,15),2);
									if(OF_rs1==EX_rd||OF_rs2==EX_rd)
									{
										OF_EX_Latch.setinstruction(null);
										IF_OF_Latch.setislock(true);
										Statistics.setnumber_of_times_OF_stalled_for_data_locks(Statistics.getnumber_of_times_OF_stalled_for_data_locks()+1);
										return;
									}
									if(EX_opcode==6||EX_opcode==7)
									{
										if(OF_rs1==31||OF_rs2==31)
										{
											OF_EX_Latch.setinstruction(null);
											IF_OF_Latch.setislock(true);
											Statistics.setnumber_of_times_OF_stalled_for_data_locks(Statistics.getnumber_of_times_OF_stalled_for_data_locks()+1);
											return;
										}
									}
								}
								else if(OF_opcode==24)
								{
									int immediate=(Ofinstruction<<10)>>10;
									if(immediate==0)
									{
										int rs1=Integer.parseInt(OF_ansstring.substring(5,10),2);
										if(rs1==EX_rd)
										{
											OF_EX_Latch.setinstruction(null);
											IF_OF_Latch.setislock(true);
											Statistics.setnumber_of_times_OF_stalled_for_data_locks(Statistics.getnumber_of_times_OF_stalled_for_data_locks()+1);
											return;
										}
										if(EX_opcode==6||EX_opcode==7)
										{
											if(rs1==31)
											{
												OF_EX_Latch.setinstruction(null);
												IF_OF_Latch.setislock(true);
												Statistics.setnumber_of_times_OF_stalled_for_data_locks(Statistics.getnumber_of_times_OF_stalled_for_data_locks()+1);
												return;
											}
										}
									}
								}
								else
								{
									int OF_rs1=Integer.parseInt(OF_ansstring.substring(5,10),2);
									if(OF_rs1==EX_rd)
									{
										OF_EX_Latch.setinstruction(null);
										IF_OF_Latch.setislock(true);											
										Statistics.setnumber_of_times_OF_stalled_for_data_locks(Statistics.getnumber_of_times_OF_stalled_for_data_locks()+1);
										return;
									}
									if(EX_opcode==6||EX_opcode==7)
									{
										if(OF_rs1==31)
										{
											OF_EX_Latch.setinstruction(null);
											IF_OF_Latch.setislock(true);
											Statistics.setnumber_of_times_OF_stalled_for_data_locks(Statistics.getnumber_of_times_OF_stalled_for_data_locks()+1);
											return;
										}
									}
								}
							}
							IF_EnableLatch.setislocked(false);
						}	
					}
					if(IF_OF_Latch.getMAinstruction()!=null)
					{
						Instruction MA_instruction=IF_OF_Latch.getMAinstruction();
						int MA_opcode=MA_instruction.getOperationType().ordinal();
						if ((MA_opcode<23))
						{
							int MA_rd=MA_instruction.getDestinationOperand().getValue();
							int Ofinstruction=inst;
							String OF_ansstring=String.format("%32s", Integer.toBinaryString(Ofinstruction)).replace(' ', '0');
							int OF_opcode=Integer.parseInt(OF_ansstring.substring(0,5),2);
							if(OF_opcode!=29)
							{
								if((OF_opcode%2==0 && OF_opcode<=21)||(OF_opcode>=25 && OF_opcode<=28)||(OF_opcode==23))
								{
									int OF_rs1=Integer.parseInt(OF_ansstring.substring(5,10),2);
									int OF_rs2=Integer.parseInt(OF_ansstring.substring(10,15),2);
									if(OF_rs1==MA_rd||OF_rs2==MA_rd)
									{
										OF_EX_Latch.setinstruction(null);
										IF_OF_Latch.setislock(true);
										Statistics.setnumber_of_times_OF_stalled_for_data_locks(Statistics.getnumber_of_times_OF_stalled_for_data_locks()+1);
										return;
									}
									if(MA_opcode==6||MA_opcode==7)
									{
										if(OF_rs1==31||OF_rs2==31)
										{
											OF_EX_Latch.setinstruction(null);
											IF_OF_Latch.setislock(true);
											Statistics.setnumber_of_times_OF_stalled_for_data_locks(Statistics.getnumber_of_times_OF_stalled_for_data_locks()+1);
											return;
										}
									}
								}
								else if(OF_opcode==24)
								{
									int immediate=(Ofinstruction<<10)>>10;
									if(immediate==0)
									{
										int rs1=Integer.parseInt(OF_ansstring.substring(5,10),2);
										if(rs1==MA_rd)
										{
											OF_EX_Latch.setinstruction(null);
											IF_OF_Latch.setislock(true);
											Statistics.setnumber_of_times_OF_stalled_for_data_locks(Statistics.getnumber_of_times_OF_stalled_for_data_locks()+1);
											return;
										}
										if(MA_opcode==6||MA_opcode==7)
										{
											if(rs1==31)
											{
												OF_EX_Latch.setinstruction(null);
												IF_OF_Latch.setislock(true);
												Statistics.setnumber_of_times_OF_stalled_for_data_locks(Statistics.getnumber_of_times_OF_stalled_for_data_locks()+1);
												return;
											}
										}
									}
								}
								else
								{
									int OF_rs1=Integer.parseInt(OF_ansstring.substring(5,10),2);
									if(OF_rs1==MA_rd)
									{
										OF_EX_Latch.setinstruction(null);
										IF_OF_Latch.setislock(true);
										Statistics.setnumber_of_times_OF_stalled_for_data_locks(Statistics.getnumber_of_times_OF_stalled_for_data_locks()+1);
										return;
									}
									if(MA_opcode==6||MA_opcode==7)
									{
										if(OF_rs1==31)
										{
											OF_EX_Latch.setinstruction(null);
											IF_OF_Latch.setislock(true);
											Statistics.setnumber_of_times_OF_stalled_for_data_locks(Statistics.getnumber_of_times_OF_stalled_for_data_locks()+1);
											return;
										}
									}
								}
							}
							IF_EnableLatch.setislocked(false);
						}	
					}
					if(IF_OF_Latch.getRWinstruction()!=null)
					{
						Instruction RW_instruction=IF_OF_Latch.getRWinstruction();
						int RW_opcode=RW_instruction.getOperationType().ordinal();
						if ((RW_opcode<23))
						{
							int RW_rd=RW_instruction.getDestinationOperand().getValue();
							int Ofinstruction=inst;
							String OF_ansstring=String.format("%32s", Integer.toBinaryString(Ofinstruction)).replace(' ', '0');
							int OF_opcode=Integer.parseInt(OF_ansstring.substring(0,5),2);
							if(OF_opcode!=29)
							{
								if((OF_opcode%2==0 && OF_opcode<=21)||(OF_opcode>=25 && OF_opcode<=28)||(OF_opcode==23))
								{
									int OF_rs1=Integer.parseInt(OF_ansstring.substring(5,10),2);
									int OF_rs2=Integer.parseInt(OF_ansstring.substring(10,15),2);
									if(OF_rs1==RW_rd||OF_rs2==RW_rd)
									{
										OF_EX_Latch.setinstruction(null);
										IF_OF_Latch.setislock(true);
										Statistics.setnumber_of_times_OF_stalled_for_data_locks(Statistics.getnumber_of_times_OF_stalled_for_data_locks()+1);
										return;
									}
									if(RW_opcode==6||RW_opcode==7)
									{
										if(OF_rs1==31||OF_rs2==31)
										{
											OF_EX_Latch.setinstruction(null);
											IF_OF_Latch.setislock(true);
											Statistics.setnumber_of_times_OF_stalled_for_data_locks(Statistics.getnumber_of_times_OF_stalled_for_data_locks()+1);
											return;
										}
									}
								}
								else if(OF_opcode==24)
								{
									int immediate=(Ofinstruction<<10)>>10;
									if(immediate==0)
									{
										int rs1=Integer.parseInt(OF_ansstring.substring(5,10),2);
										if(rs1==RW_rd)
										{
											OF_EX_Latch.setinstruction(null);
											IF_OF_Latch.setislock(true);
											Statistics.setnumber_of_times_OF_stalled_for_data_locks(Statistics.getnumber_of_times_OF_stalled_for_data_locks()+1);
											return;
										}
										if(RW_opcode==6||RW_opcode==7)
										{
											if(rs1==31)
											{
												OF_EX_Latch.setinstruction(null);
												IF_OF_Latch.setislock(true);
												Statistics.setnumber_of_times_OF_stalled_for_data_locks(Statistics.getnumber_of_times_OF_stalled_for_data_locks()+1);
												return;
											}
										}
									}
								}
								else
								{
									int OF_rs1=Integer.parseInt(OF_ansstring.substring(5,10),2);
									if(OF_rs1==RW_rd)
									{
										OF_EX_Latch.setinstruction(null);
										IF_OF_Latch.setislock(true);
										Statistics.setnumber_of_times_OF_stalled_for_data_locks(Statistics.getnumber_of_times_OF_stalled_for_data_locks()+1);
										return;
									}
									if(RW_opcode==6||RW_opcode==7)
									{
										if(OF_rs1==31)
										{
											OF_EX_Latch.setinstruction(null);
											IF_OF_Latch.setislock(true);
											Statistics.setnumber_of_times_OF_stalled_for_data_locks(Statistics.getnumber_of_times_OF_stalled_for_data_locks()+1);
											return;
										}
									}
								}
							}
						}
						IF_EnableLatch.setislocked(false);
					}
					String ansstring=String.format("%32s", Integer.toBinaryString(inst)).replace(' ', '0');
					int opcode=Integer.parseInt(ansstring.substring(0,5),2);
					instruction.setOperationType(OperationType.values()[opcode]);
					Operand sourceoperand1=new Operand();
					Operand sourceoperand2=new Operand();
					Operand destinationoperand= new Operand();
					if(opcode%2==0 && opcode<=21)
					{
						int rs1=Integer.parseInt(ansstring.substring(5,10),2);
						int rs2=Integer.parseInt(ansstring.substring(10,15),2);
						int rd=Integer.parseInt(ansstring.substring(15,20),2);
						sourceoperand1.setOperandType(OperandType.Register);
						sourceoperand2.setOperandType(OperandType.Register);
						destinationoperand.setOperandType(OperandType.Register);
						sourceoperand1.setValue(containingProcessor.getRegisterFile().getValue(rs1));
						sourceoperand2.setValue(containingProcessor.getRegisterFile().getValue(rs2));
						destinationoperand.setValue(rd);
						instruction.setSourceOperand1(sourceoperand1);
						instruction.setSourceOperand2(sourceoperand2);
						instruction.setDestinationOperand(destinationoperand);

					}
					else if(opcode==29)
					{
						sourceoperand1.setOperandType(OperandType.Register);
						sourceoperand2.setOperandType(OperandType.Register);
						destinationoperand.setOperandType(OperandType.Register);
						sourceoperand1.setValue(0);
						sourceoperand2.setValue(0);
						destinationoperand.setValue(0);
						instruction.setSourceOperand1(sourceoperand1);
						instruction.setSourceOperand2(sourceoperand2);
						instruction.setDestinationOperand(destinationoperand);
						IF_OF_Latch.setisprogramfinished(true);
					}
					else if(opcode==24)
					{
						int rd=Integer.parseInt(ansstring.substring(5,10 ),2);
						int immediate=(inst<<10)>>10;
						if(immediate==0)
						{
							destinationoperand.setOperandType(OperandType.Register);
							destinationoperand.setValue(containingProcessor.getRegisterFile().getValue(rd)+containingProcessor.getRegisterFile().getProgramCounter()-1);
							instruction.setDestinationOperand(destinationoperand);

						}
						else
						{
							destinationoperand.setOperandType(OperandType.Label);
							immediate=immediate+containingProcessor.getRegisterFile().getProgramCounter()-1;
							destinationoperand.setValue(immediate);
							instruction.setDestinationOperand(destinationoperand);
						}
						sourceoperand1.setOperandType(OperandType.Register);
						sourceoperand2.setOperandType(OperandType.Register);
						sourceoperand1.setValue(0);
						sourceoperand2.setValue(0);
						instruction.setSourceOperand1(sourceoperand1);
						instruction.setSourceOperand2(sourceoperand2);
					}
					else if (opcode>=25 && opcode<=28)
					{
						int rs1=Integer.parseInt(ansstring.substring(5,10),2);
						int rs2=Integer.parseInt(ansstring.substring(10,15),2);
						int rd=(inst<<15)>>15;
						sourceoperand1.setOperandType(OperandType.Register);
						sourceoperand2.setOperandType(OperandType.Register);
						sourceoperand1.setValue(containingProcessor.getRegisterFile().getValue(rs1));
						sourceoperand2.setValue(containingProcessor.getRegisterFile().getValue(rs2));
						destinationoperand.setOperandType(OperandType.Label);
						instruction.setSourceOperand1(sourceoperand1);
						instruction.setSourceOperand2(sourceoperand2);
						rd=rd+containingProcessor.getRegisterFile().getProgramCounter()-1;
						destinationoperand.setValue(rd);
						instruction.setDestinationOperand(destinationoperand);
					}
					else
					{
						int rs1=Integer.parseInt(ansstring.substring(5,10),2);
						int rd=Integer.parseInt(ansstring.substring(10,15),2);
						int rs2=(inst<<15)>>15;
						sourceoperand1.setOperandType(OperandType.Register);
						sourceoperand2.setOperandType(OperandType.Immediate);
						sourceoperand1.setValue(containingProcessor.getRegisterFile().getValue(rs1));
						sourceoperand2.setValue(rs2);
						destinationoperand.setOperandType(OperandType.Register);
						destinationoperand.setValue(rd);
						instruction.setSourceOperand1(sourceoperand1);
						instruction.setSourceOperand2(sourceoperand2);
						instruction.setDestinationOperand(destinationoperand);
					}
					IF_OF_Latch.setislock(false);
					OF_EX_Latch.setinstruction(instruction);
				}
			}
		}
		else
		{
			IF_OF_Latch.setisprogramfinished(true);
		}
	}
}
