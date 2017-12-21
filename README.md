# hangman-server
Server endpoints to support the game Hangman

Create a simple back-end service that supports playing the word game 'Hangman'. https://en.wikipedia.org/wiki/Hangman_(game)
 
The rest end points are follows 
1. Start a new game - returns a game id along with a secret word. The word is chosen by server at random from a list of words that you can supply from a dictionary or something. The secret word is returned as a string where letters are replaced with '_'
2. Guess a word with a letter. The input for this end point are the game id and the character. The return value is the secret word with only the correctly guessed letters and the number of incorrect guesses so far and the status of the game one of (ACTIVE, WON, LOST). The game is lost in incorrect guesses is 7.   
 
Example 
API Call                                |     Output
----------------------------------------|--------------------------------------------------------------------
new                                     |    {gameId: 'cdaeaa', word: '____'}
guess {game: 'cdaeaa', guess: 'e' }     |    {gameId: 'cdaeaa', word: '____', incorrect: 1, status: 'ACTIVE'}
guess {game: 'cdaeaa', guess: 'a' }     |    {gameId: 'cdaeaa', word: '_a__', incorrect: 1, status: 'ACTIVE'}
guess {game: 'cdaeaa', guess: 'r' }     |    {gameId: 'cdaeaa', word: '_a__', incorrect: 2, status: 'ACTIVE'}
guess {game: 'cdaeaa', guess: 's' }     |   {gameId: 'cdaeaa', word: '_a__', incorrect: 3, status: 'ACTIVE'}
guess {game: 'cdaeaa', guess: 'z' }     |   {gameId: 'cdaeaa', word: '_azz', incorrect: 3, status: 'ACTIVE'}
guess {game: 'cdaeaa', guess: 'j' }     |   {gameId: 'cdaeaa', word: 'jazz', incorrect: 3, status: 'WON'}
guess {game: 'cdaeaa', guess: 'f' }     |   {error: 'Game is already complete'}
 
You can use whatever Java based framework (Spring Boot, DropWizard or plain Java) to create the back-end service. 
