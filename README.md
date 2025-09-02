# Game Center - Android Multi-Game App

A modern Android application featuring multiple classic games with beautiful Material Design UI and smooth animations.

## 🎮 **Available Games**

### 1. **Tic Tac Toe**
- **Two Game Modes:**
  - VS Computer: Play against a strategic AI opponent
  - VS Friend: Play against another player on the same device
- **Smart AI Strategy:**
  - Looks for winning moves and blocks opponent's winning moves
  - Prefers center and corner positions for optimal play
- **Modern UI:** Clean grid layout with smooth animations

### 2. **Water Jug Puzzle**
- **Classic Logic Puzzle:** Solve water jug problems with different capacities
- **Dynamic Puzzles:** Randomly generated solvable puzzles
- **Visual Water Effects:** Beautiful water animations with color intensity changes
- **Smart Button Management:** Buttons automatically disable for invalid moves
- **Move Counter:** Track your solution efficiency

## ✨ **Key Features**

- **Unified Game Center:** Access all games from a single main menu
- **Material Design 3:** Modern, beautiful UI following Google's design guidelines
- **Responsive Layout:** Optimized for different screen sizes and orientations
- **Smooth Animations:** Engaging visual feedback for all game interactions
- **Smart Game Logic:** Intelligent AI and puzzle validation
- **Beautiful App Icon:** Custom gamepad icon with adaptive icon support

## 🏗️ **Project Structure**

```
Game_center/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/tictactoe/
│   │   │   ├── MainActivity.kt              # Main menu and game selection
│   │   │   ├── GameActivity.kt              # Tic Tac Toe game logic
│   │   │   └── WaterJugActivity.kt         # Water Jug puzzle logic
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_main.xml        # Main menu layout
│   │   │   │   ├── activity_game.xml        # Tic Tac Toe board layout
│   │   │   │   ├── activity_water_jug.xml   # Water Jug puzzle layout
│   │   │   │   └── item_cell.xml            # Tic Tac Toe cell layout
│   │   │   ├── drawable/
│   │   │   │   ├── water_background.xml     # Water effect background
│   │   │   │   ├── water_surface.xml        # Water surface effect
│   │   │   │   ├── ic_launcher_foreground.xml # App icon foreground
│   │   │   │   └── ic_launcher_background.xml # App icon background
│   │   │   ├── values/
│   │   │   │   ├── colors.xml               # Color definitions
│   │   │   │   ├── strings.xml              # String resources
│   │   │   │   └── themes.xml               # App theme
│   │   │   └── mipmap-anydpi-v26/
│   │   │       ├── ic_launcher.xml          # Adaptive icon config
│   │   │       └── ic_launcher_round.xml    # Round icon config
│   │   └── AndroidManifest.xml              # App manifest
│   ├── build.gradle                         # App-level build configuration
│   └── proguard-rules.pro                  # ProGuard rules
├── img/
│   ├── gamepad.svg                          # Original SVG icon
│   └── gamepad.xml                          # Converted vector drawable
├── build.gradle                             # Project-level build configuration
├── settings.gradle                          # Project settings
├── gradle.properties                        # Gradle properties
└── README.md                                # This file
```

## 🚀 **Requirements**

- **Android Studio:** Arctic Fox (2020.3.1) or later
- **Android SDK:** API level 26 (Android 8.0) or higher
- **Kotlin:** 1.9.22 or higher
- **Gradle:** 8.2.2 or higher
- **Minimum Device:** Android 8.0 (API 26) or higher

## 🔧 **Building the App**

1. **Clone or download the project files**
2. **Open Android Studio**
3. **Open the project** by selecting the `Game_center` folder
4. **Wait for Gradle sync** to complete
5. **Connect an Android device** or start an emulator
6. **Click the Run button** (green play button) or press `Shift+F10`

## 🎯 **Game Rules**

### **Tic Tac Toe**
- Players take turns placing X and O on a 3x3 grid
- First player to get 3 in a row (horizontally, vertically, or diagonally) wins
- If all cells are filled without a winner, the game is a draw
- X always goes first

### **Water Jug Puzzle**
- You have two jugs with different capacities
- Goal: Measure exactly the target amount using the jugs
- Actions available:
  - Fill a jug to capacity
  - Empty a jug completely
  - Pour water from one jug to another
- Buttons automatically disable for invalid moves
- Move counter tracks your solution efficiency

## 🧠 **AI Strategy (Tic Tac Toe)**

The computer player uses the following strategy (in order of priority):
1. **Winning Move**: If the computer can win in one move, it takes it
2. **Blocking Move**: If the opponent can win in one move, the computer blocks it
3. **Center Position**: Prefers the center cell if available
4. **Corner Positions**: Prefers corner cells if available
5. **Random Move**: Makes a random move if no strategic options exist

## 🎨 **Customization**

You can easily customize the app by modifying:

- **Colors**: Edit `app/src/main/res/values/colors.xml`
- **Strings**: Edit `app/src/main/res/values/strings.xml`
- **Theme**: Edit `app/src/main/res/values/themes.xml`
- **Game Logic**: Modify respective game activity files
- **App Icon**: Replace `img/gamepad.svg` and regenerate drawables

## 🐛 **Troubleshooting**

### **Build Issues**
- **Gradle sync errors**: Try `File → Invalidate Caches and Restart`
- **Build failures**: Ensure correct Android SDK and build tools are installed
- **File lock errors**: Stop Gradle daemons with `.\gradlew --stop`

### **Runtime Issues**
- **App crashes**: Check that your device meets minimum API level requirement (26)
- **Layout issues**: Verify device orientation and screen size compatibility
- **Performance**: Close other apps to free up memory

## 📱 **App Icon**

The app features a custom gamepad icon:
- **Original Design**: Beautiful SVG gamepad with purple/blue color scheme
- **Adaptive Icon**: Automatically adapts to different icon shapes (circle, square, rounded)
- **Vector Drawable**: Scalable without quality loss
- **Theme Support**: Includes monochrome version for Android 13+ theme adaptation

## 📄 **License**

This project is open source and available under the MIT License.

## 🤝 **Contributing**

We welcome contributions! Feel free to:
- Submit bug reports and feature requests
- Contribute code improvements
- Add new games to the center
- Enhance existing game logic
- Improve UI/UX design

## 🔮 **Future Plans**

- **Additional Games**: Chess, Sudoku, Memory Match
- **Online Multiplayer**: Play with friends over the internet
- **Achievement System**: Unlock achievements and track progress
- **Custom Themes**: Multiple color schemes and themes
- **Sound Effects**: Audio feedback for better gaming experience

---

**Enjoy playing! 🎮✨**
