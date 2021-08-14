# ColorPickerDialog
Java class for android to display a color selector dialog which I wrote for my own projects which I decided to release as open source.


## Screenshots
![gridpicker](https://user-images.githubusercontent.com/14188843/129220433-6db38ad0-c48d-4eb6-92b9-2d8ae96a975f.jpg) ![circlepicker](https://user-images.githubusercontent.com/14188843/129220481-3c249246-04b9-480b-88eb-74d476ef0496.jpg)

## How to use
Just add the file in you android project.

To create the dialog you must use:

```java
ColorPickerDialog colorPickerDialog = new ColorPickerDialog(
                    context,
                    (int)initial_color,
                    "title of dialog",
                    "text for OK button",
                    "text for cancel button",
                    "text for More button",
                    this::textView.setColor); //listener method it will pass the selector color as int
```

You can show either the grid picker dialog or circle picker by using:

```java
colorPickerDialog.showGridPicker();

colorPickerDialog.showCirclePicker();
```

## Changelog

Version 1.0
* Initial version
