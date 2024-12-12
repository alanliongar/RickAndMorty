## Rick and Morty APP
Rick and Morty APP is an Android application designed to provide an engaging and user-friendly experience for fans of the show. This app displays characters from the Rick and Morty universe, allowing users to filter, search, and favorite their favorite characters while showcasing efficient offline data handling and clean architecture.

Features
- Character List: View a complete list of characters from Rick and Morty.
- Search Functionality: Use the search bar to find characters by name quickly.
- Filter by Species: Filter characters by their species using the dropdown spinner.
- Favorites Management: Favorite your preferred characters and toggle the floating action button to display only favorited characters.
- Offline Support: Access previously loaded data without an internet connection, thanks to the Repository Pattern (Offline First).
- Dynamic Floating Action Button: Tap to toggle between showing all characters or only favorited ones.
:camera_flash: Screenshots
<p float="left"> 
  <img src="https://github.com/alanliongar/rickandmorty/blob/master/Screenshot_20241212_202308.png" width="250" /> 
  <img src="https://github.com/alanliongar/rickandmorty/blob/master/Screenshot_20241212_202320.png" width="250" /> 
  <img src="https://github.com/alanliongar/rickandmorty/blob/master/Screenshot_20241212_202421.png" width="250" />
</p>

## Technologies
- 100% Kotlin
- Repository Pattern: For scalable and maintainable data handling, with offline-first capability.
- MVVM Architecture: Ensures a clear separation of concerns for cleaner code structure.
- Compose
  - Column
  - Row
  - Modifier
  - Spacer
  - LazyRows
  - LazyColumns
  - TextField (Search Bar)
  - FloatingActionButton
  - Spinner (Dropdown)
  - ComposePreview
  - NavHostController
  - AsyncImage
- Coil: Used for rendering GIFs and SVGs efficiently
- Palette: Extracts the dominant color from character images and applies it to the card backgrounds for a cohesive UI
- Room Database
- Retrofit
- Rick and Morty API
- Unit Tests
  - Mockito-Kotlin
  - Turbine
  - Fakes
 
## License
```
The MIT License (MIT)

Copyright (c) 2024 Alan Lucindo Gomes

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```
