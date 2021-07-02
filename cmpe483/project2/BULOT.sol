pragma solidity ^0.8.0;

// SPDX-License-Identifier: AGPL-3.0-only

contract ERC20Token {

    uint256 constant private MAX_UINT256 = 2**256 - 1;
    mapping (address => uint256) public balances;
    mapping (address => mapping (address => uint256)) public allowed;
    /*
    NOTE:
    The following variables are OPTIONAL vanities. One does not have to include them.
    They allow one to customise the token contract & in no way influences the core functionality.
    Some wallets/interfaces might not even bother to look at this information.
    */
    string public name;                   //fancy name: eg Simon Bucks
    uint8 public decimals;                //How many decimals to show.
    string public symbol;                 //An identifier: eg SBX
    uint256 totalSupply ; 
    
    event Transfer(address,address,uint256) ;
    event Approval(address,address,uint256);

    constructor(uint256 _initialAmount,string memory _tokenName,uint8 _decimalUnits,
                string memory _tokenSymbol)  {
        balances[msg.sender] = _initialAmount;               // Give the creator all initial tokens
        totalSupply = _initialAmount;                        // Update total supply
        name = _tokenName;                                   // Set the name for display purposes
        decimals = _decimalUnits;                            // Amount of decimals for display purposes
        symbol = _tokenSymbol;                               // Set the symbol for display purposes
    }

    function transfer(address _to, uint256 _value) public returns (bool success) {
        require(balances[msg.sender] >= _value);
        balances[msg.sender] -= _value;
        balances[_to] += _value;
        emit Transfer(msg.sender, _to, _value); //solhint-disable-line indent, no-unused-vars
        return true;
    }
                             
    function transferFrom(address _from, address _to, uint256 _value) public returns (bool success) {
        uint256 allowans = allowed[_from][msg.sender];
        require(balances[_from] >= _value && allowans >= _value);
        balances[_to] += _value;
        balances[_from] -= _value;
        if (allowans < MAX_UINT256) {
            allowed[_from][msg.sender] -= _value;
        }
        emit Transfer(_from, _to, _value); //solhint-disable-line indent, no-unused-vars
        return true;
    }

    function balanceOf(address _owner) public view returns (uint256 balance) {
        return balances[_owner];
    }

    function approve(address _spender, uint256 _value) public returns (bool success) {
        allowed[msg.sender][_spender] = _value;
        emit Approval(msg.sender, _spender, _value); //solhint-disable-line indent, no-unused-vars
        return true;
    }

    function allowance(address _owner, address _spender) public view returns (uint256 remaining) {
        return allowed[_owner][_spender];
    }
}


contract BULOT {
   
  
   // total customer number
   uint public numcustomers ;  
   
   // weekly customer number
   uint weeklycustomers ;  
   
   
   // weekly index  (ex. -> First customer index is 1.)
   uint weeklyindex;    // TODOO
   
   uint public dmod;
   
   // balance 
   mapping(address => uint) public balance ;
   
   // user -> her ticket numbers for each lottery
   /* (example)
      1. week -> 5,6,7  ticketno[msg.sender][1] = {5,6,7}
      2. week -> 3,9,12  ticketno[msg.sender][2] = {3,9,12}  */
   mapping(address => mapping(uint => uint[])) public ticketnos ; 
   
   // user -> chance of winning of her ticket numbers for each lottery
   /* (example)
      1. week -> 5,6,7  ticketnobools[1][1] = true  ticket with number 1 in first lottery is true
      2. week -> 3,9,12  ticketnobools[1][1] = true   */
   mapping(uint => mapping(uint => bool)) public ticketnobools ; 
   
   // total number of tickets in a week
   mapping(uint => uint) public weekNums ;
   
   
   // lottery no -> hashes for each ticket number
   /* (example)
      1. week -> 1,2,3,4,5 (5 tickets sold) ticket_no[1][2] = hash of 2nd ticket which is sold in 1st lottery
      2. week -> 1,2,3,4,5,6,7 (7 tickets sold) ticket_no[2][4] = hash of 4th ticket which is sold in 2nd lottery  */
   mapping(uint => mapping(uint => bytes32)) public hashes ; 
   
   // money collected for each lottery
   /* (example)
      moneyCollected[1] = 430  (430 TL are collected for the first lottery-43 people bought a ticket-)      */
   mapping(uint => uint) public moneyCollected; 
   
   // xor value for each lottery
   /* (example)
      xorValue[1] = 52  (xorValue for 1st lottery is 52)      */
   mapping(uint => uint) public xorValue; 
   
   
   // winning ticket numbers for each lottery
   /* (example)
      winningTicketNos[1][2] = 3  (2nd winning ticket number in 1st lottery is 3)  */
   mapping(uint => mapping(uint => uint)) public winningTicketNos; 
   
   // ith winning ticket prizes for each lottery
   /* (example)
      winningTicketNoPrizes[1][2] = 3  (2nd winning ticket prize in 1st lottery is 3)  */
   mapping(uint => mapping(uint => uint)) public winningTicketNoPrizes; 
   
   // winning ticket prizes for each lottery
   /* (example)
      winningTicketPrizes[1][2] = 30  (Prize of the ticket with ticket number 2 in 1st lottery is 30 TL)  */
   mapping(uint => mapping(uint => uint)) public winningTicketPrizes; 
   
   // winning ticket prizes for each lottery
   /* (example)
      isPrizeWithdrawed[1][2] = true  (Prize of the 2nd ticket number in 1st lottery is already withdrawed.)  */
   mapping(uint => mapping(uint => bool)) public isPrizeWithdrawed; 
   

   // address of the ERC20 Token address 
   address public contractaddr ; 
   
   // lottery number
   uint	public lotteryno	;
   
   // start period time
   uint	public startperiod	;
   
   // end period time
   uint	public endperiod	;	

   constructor(address conaddr)  {
      numcustomers = 0 ;  // customer number is 0 at initialization stage
      weeklycustomers = 0 ;  // customer number for a week is 0 at initialization stage
      contractaddr = conaddr ; // ERC20 Token Address
      lotteryno = 1;  // to start with 1st lottery
      startperiod = block.timestamp;  // start period time is current time
      endperiod = startperiod + 7 days;  // next period is starting after one week, so end period is start + 7 days
      weeklyindex = 0;  
   }

 
    // This function is simulating the buying ticket event.
    function buyTicket(bytes32 hash_rnd_number) public  {
        
         // updates lottery number(if needed)
         getCurrentLotteryNo();
         
         // gets the contract 
         ERC20Token contractobj = ERC20Token(contractaddr) ; 
         
         // ticket price is 10 TL for this project
         uint256 ticketPrice = 10;
         
         if (ticketPrice > 0) {
           // if this user enters the lottery for the first time, related customer numbers should be increased by 1.     
           if (balance[msg.sender] == 0) {
             balance[msg.sender] = contractobj.balanceOf(msg.sender);  
             numcustomers = numcustomers + 1 ;  
             weeklycustomers = weeklycustomers + 1 ; 
           }
           // transfers the ticket payment to her account and her wallet is decreased
           contractobj.transferFrom(msg.sender,address(this),ticketPrice) ; 
           balance[msg.sender]  -= ticketPrice; 
         }
         // her ticket number is saved 
         ticketnos[msg.sender][lotteryno].push(weeklyindex);    
         ticketnobools[lotteryno][weeklyindex]=true;    
         
         // her hash is saved
         hashes[lotteryno][weeklyindex] = hash_rnd_number;
         
         // total money collected for that lottery is increased
         moneyCollected[lotteryno] += ticketPrice;
         
         
         // weekly index should be increased.
         weeklyindex = weeklyindex + 1; 
     
    }

  
  // This function is simulating the reveal random number event.
  function revealRndNumber(uint ticketno, uint rnd_number) public {
      
      // updates lottery number(if needed)
      getCurrentLotteryNo();  
      
      // if in first submission stage, there is no reveal period yet.
      if(lotteryno!=1) {  
      
        // gets the hash of ticket, which is submitted on previous week
        bytes32 hash = hashes[lotteryno-1][ticketno];  
        
        // finds hash of the random number by concatenating the number with the account address
        /* concatenate function is taken from: https://ethereum.stackexchange.com/questions/52698/how-to-typecast-keccak256-function-with-multiple-arguments-to-uint256 */
        bytes32 correctHash = keccak256(abi.encodePacked(rnd_number,msg.sender));
        
      
        // if the calculated hash does not match the given hash which is given in submission stage, the chance of winning is lost
        if(correctHash==hash) {
            xorValue[lotteryno-1] = xorValue[lotteryno-1]^rnd_number;  
        } else {
            // sets the validity of ticket as false (chance of winning is lost)
            ticketnobools[lotteryno][ticketno]=false; 
        }
      }

  }
  
  // user's last bought ticket number for a lottery
  function getLastBoughtTicketNo(uint lottery_no) public view returns(uint) {
      // TODO
      uint length = ticketnos[msg.sender][lottery_no].length; // user's total number of ticket
      if(length>=0)
      return(ticketnos[msg.sender][lottery_no][length-1]);
      else
      return 0;
  }
  
  // user's i'th bought ticket number for a lottery
  function getIthBoughtTicketNo(uint i, uint lottery_no) public view returns(uint) {
      return(ticketnos[msg.sender][lottery_no][i-1]);
  }

  
  // returns current lottery number
  function getCurrentLotteryNo() public returns (uint lottery_no) {
      // when period ends   
      
      // TODO
      if(block.timestamp > endperiod)	{	// lottery ended, winners should be calculated
      // finds how many lotteries have passed
         uint multiplier = (block.timestamp-startperiod)/(7 days);
         if(lotteryno!=1) {  // for first lottery, there's no reveal period yet. (because of overlapping manner)
         
            // find mod of xor value for previous lottery
            uint mod = xorValue[lotteryno-1] % weekNums[lotteryno-1]; 
            
            uint numOfWinners = 0;
            // total money collected for previous lottery
            uint totalMoney = moneyCollected[lotteryno-1];
            uint totalMoneyx = moneyCollected[lotteryno-1];
            
            // calculates log2M operation
            /* log operation with gas efficiency, 
               taken from: https://medium.com/coinmonks/math-in-solidity-part-5-exponent-and-logarithm-9aef8515136e */
            if (totalMoneyx >= 2**128) { totalMoneyx >>= 128; numOfWinners += 128; }
            if (totalMoneyx >= 2**64) { totalMoneyx >>= 64; numOfWinners += 64; }
            if (totalMoneyx >= 2**32) { totalMoneyx >>= 32; numOfWinners += 32; }
            if (totalMoneyx >= 2**16) { totalMoneyx >>= 16; numOfWinners += 16; }
            if (totalMoneyx >= 2**8) { totalMoneyx >>= 8; numOfWinners += 8; }
            if (totalMoneyx >= 2**4) { totalMoneyx >>= 4; numOfWinners += 4; }
            if (totalMoneyx >= 2**2) { totalMoneyx >>= 2; numOfWinners += 2; } 
            if (totalMoneyx >= 2**1) {  numOfWinners += 1; } 
            
            
            if(2**numOfWinners!=totalMoney) {
              numOfWinners = numOfWinners+1; 
            }
            
            dmod = numOfWinners;

        
            uint prev=0;
            if(ticketnobools[lotteryno-1][mod]) {
                // first winner's ticket number is saved
                winningTicketNos[lotteryno-1][1] = mod; // 
                prev = xorValue[lotteryno-1];
                uint prize = (totalMoney/2)+((totalMoney)%2);  
                // first prize is saved
                winningTicketPrizes[lotteryno-1][mod] = prize;
                winningTicketNoPrizes[lotteryno-1][1] = prize;
            }   

       
            
            // finding remaning winning ticket numbers & prizes
            for(uint i=2; i<=numOfWinners; i++) {
                 bytes32 currentHash = keccak256(abi.encodePacked(prev));
                 uint ticketNum = uint(currentHash) % weekNums[lotteryno-1]; 
                 if(ticketnobools[lotteryno-1][ticketNum]) {
                    winningTicketNos[lotteryno-1][i]= ticketNum;
                    // calculating prize
                    uint prize = (totalMoney/2**i)+((totalMoney/(2**(i-1)))%2); 
                    // saving prize
                    winningTicketPrizes[lotteryno-1][ticketNum] += prize;
                    winningTicketNoPrizes[lotteryno-1][i] = prize;
                    prev = uint(currentHash);
                 }
                 
            } 

         }   
            // updating the lottery number & stage
			startperiod	=	startperiod + multiplier*7 days	;	
			endperiod	=	startperiod	+	7 days ;	
			weeklycustomers = 0;
			weekNums[lotteryno] = weeklyindex;
			lotteryno+= multiplier	;
			weeklyindex = 0;
	  }	
	  
      return(lotteryno);
  }
  
  // returns money collected for each lottery
  function getMoneyCollected(uint lottery_no) public view returns (uint	amount) {
      return(moneyCollected[lottery_no]);
  }
  
  
  // returns the prize of each ticket in a lottery
  function checkIfTicketWon(uint lottery_no, uint ticket_no) public	view returns (uint amount) {
      uint prize = winningTicketPrizes[lottery_no][ticket_no];
      return(prize);
  }
  
  // This function simulates the withdrawing ticket prize event
  function withdrawTicketPrize(uint lottery_no, uint ticket_no) public	{
        // User can withdraw her prize only if the lottery round ends.
        if(lotteryno>lottery_no) {  
            ERC20Token contractobj = ERC20Token(contractaddr) ;  
            // gets the prize of that ticket and withdraws it from user's account
            uint amount = checkIfTicketWon(lottery_no,ticket_no);  
            if (balance[msg.sender] >= amount && !isPrizeWithdrawed[lottery_no][ticket_no]) {
                balance[msg.sender] = balance[msg.sender] + amount ; 
                contractobj.transfer(msg.sender,amount) ;
                if (balance[msg.sender] == 0)  {
                    numcustomers = numcustomers - 1 ; 
                }
                isPrizeWithdrawed[lottery_no][ticket_no] = true;
            }
        }  
  }
  
  // find ith winning ticket number and its prize
  function getIthWinningTicket(uint	i, uint	lottery_no)	public view	returns	(uint ticket_no, uint amount) {
      uint winningTicketNo = winningTicketNos[lottery_no][i];
      uint prize = winningTicketNoPrizes[lottery_no][i];
      return(winningTicketNo,prize);
  }
  

  
    
  // calculates the hash of the given number
  function myHash(uint rnd_number) public view returns(bytes32) {
      return(keccak256(abi.encodePacked(rnd_number,msg.sender)));
  }

   
   
   
    
}