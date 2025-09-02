# Tic Tac Toe Android App

A modern, minimal Android Tic Tac Toe game with a clean Material Design UI.

## Features

- **Two Game Modes:**
  - VS Computer: Play against an AI opponent
  - VS Friend: Play against another player on the same device

- **Smart AI:**
  - The computer player uses strategic thinking
  - Looks for winning moves and blocks opponent's winning moves
  - Prefers center and corner positions for optimal play

- **Modern UI:**
  - Clean, minimal Material Design interface
  - Smooth animations and transitions
  - Responsive layout for different screen sizes
  - Beautiful color scheme with distinct X and O colors

## Project Structure

```
app/
├── src/main/
│   ├── java/com/example/tictactoe/
│   │   ├── MainActivity.kt          # Main menu and game mode selection
│   │   └── GameActivity.kt          # Game logic and board management
│   ├── res/
│   │   ├── layout/
│   │   │   ├── activity_main.xml    # Main menu layout
│   │   │   ├── activity_game.xml    # Game board layout
│   │   │   └── item_cell.xml        # Individual cell layout
│   │   ├── values/
│   │   │   ├── colors.xml           # Color definitions
│   │   │   ├── strings.xml          # String resources
│   │   │   └── themes.xml           # App theme
│   │   └── xml/
│   │       ├── backup_rules.xml     # Backup configuration
│   │       └── data_extraction_rules.xml
│   └── AndroidManifest.xml          # App manifest
├── build.gradle                     # App-level build configuration
└── proguard-rules.pro              # ProGuard rules

build.gradle                         # Project-level build configuration
settings.gradle                     # Project settings
gradle.properties                    # Gradle properties
```

## Requirements

- Android Studio Arctic Fox (2020.3.1) or later
- Android SDK 26 (API level 26) or higher
- Kotlin 1.9.22 or higher
- Gradle 8.2.2 or higher

## Building the App

1. **Clone or download the project files**
2. **Open Android Studio**
3. **Open the project** by selecting the project folder
4. **Wait for Gradle sync** to complete
5. **Connect an Android device** or start an emulator
6. **Click the Run button** (green play button) or press Shift+F10

## Game Rules

- Players take turns placing X and O on a 3x3 grid
- First player to get 3 in a row (horizontally, vertically, or diagonally) wins
- If all cells are filled without a winner, the game is a draw
- X always goes first

## AI Strategy

The computer player uses the following strategy (in order of priority):
1. **Winning Move**: If the computer can win in one move, it takes it
2. **Blocking Move**: If the opponent can win in one move, the computer blocks it
3. **Center Position**: Prefers the center cell if available
4. **Corner Positions**: Prefers corner cells if available
5. **Random Move**: Makes a random move if no strategic options exist

## Customization

You can easily customize the app by modifying:
- **Colors**: Edit `app/src/main/res/values/colors.xml`
- **Strings**: Edit `app/src/main/res/values/strings.xml`
- **Theme**: Edit `app/src/main/res/values/themes.xml`
- **Game Logic**: Modify `GameActivity.kt`

## Troubleshooting

- **Build errors**: Make sure you have the correct Android SDK and build tools installed
- **Runtime errors**: Check that your device/emulator meets the minimum API level requirement (26)
- **Gradle sync issues**: Try File → Invalidate Caches and Restart

## License

This project is open source and available under the MIT License.

## Contributing

Feel free to submit issues, feature requests, or pull requests to improve the game!

