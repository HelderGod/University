# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.18

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:


#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:


# Disable VCS-based implicit rules.
% : %,v


# Disable VCS-based implicit rules.
% : RCS/%


# Disable VCS-based implicit rules.
% : RCS/%,v


# Disable VCS-based implicit rules.
% : SCCS/s.%


# Disable VCS-based implicit rules.
% : s.%


.SUFFIXES: .hpux_make_needs_suffix_list


# Command-line flag to silence nested $(MAKE).
$(VERBOSE)MAKESILENT = -s

#Suppress display of executed commands.
$(VERBOSE).SILENT:

# A target that is always out of date.
cmake_force:

.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /usr/bin/cmake

# The command to remove a file.
RM = /usr/bin/cmake -E rm -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /home/gui/Documents/Uni/Redes/Sockets

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /home/gui/Documents/Uni/Redes/Sockets/build

# Include any dependencies generated for this target.
include CMakeFiles/ctest.dir/depend.make

# Include the progress variables for this target.
include CMakeFiles/ctest.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/ctest.dir/flags.make

CMakeFiles/ctest.dir/src/main.c.o: CMakeFiles/ctest.dir/flags.make
CMakeFiles/ctest.dir/src/main.c.o: ../src/main.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/gui/Documents/Uni/Redes/Sockets/build/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building C object CMakeFiles/ctest.dir/src/main.c.o"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/ctest.dir/src/main.c.o -c /home/gui/Documents/Uni/Redes/Sockets/src/main.c

CMakeFiles/ctest.dir/src/main.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/ctest.dir/src/main.c.i"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E /home/gui/Documents/Uni/Redes/Sockets/src/main.c > CMakeFiles/ctest.dir/src/main.c.i

CMakeFiles/ctest.dir/src/main.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/ctest.dir/src/main.c.s"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S /home/gui/Documents/Uni/Redes/Sockets/src/main.c -o CMakeFiles/ctest.dir/src/main.c.s

# Object files for target ctest
ctest_OBJECTS = \
"CMakeFiles/ctest.dir/src/main.c.o"

# External object files for target ctest
ctest_EXTERNAL_OBJECTS =

bin/ctest: CMakeFiles/ctest.dir/src/main.c.o
bin/ctest: CMakeFiles/ctest.dir/build.make
bin/ctest: CMakeFiles/ctest.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=/home/gui/Documents/Uni/Redes/Sockets/build/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Linking C executable bin/ctest"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/ctest.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/ctest.dir/build: bin/ctest

.PHONY : CMakeFiles/ctest.dir/build

CMakeFiles/ctest.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/ctest.dir/cmake_clean.cmake
.PHONY : CMakeFiles/ctest.dir/clean

CMakeFiles/ctest.dir/depend:
	cd /home/gui/Documents/Uni/Redes/Sockets/build && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/gui/Documents/Uni/Redes/Sockets /home/gui/Documents/Uni/Redes/Sockets /home/gui/Documents/Uni/Redes/Sockets/build /home/gui/Documents/Uni/Redes/Sockets/build /home/gui/Documents/Uni/Redes/Sockets/build/CMakeFiles/ctest.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/ctest.dir/depend

