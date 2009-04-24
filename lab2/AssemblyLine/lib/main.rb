class AssemblyLine
  # assembly_times[i][j]: how much time it takes to assemble @ line _i_ on station _j_
  #   i - line number (0, 1)
  #   j - station number (0..n)
  #
  # transfer_times[i][j]: how much time it takes to transfer from line _i_ station _j_ to other
  # line station _j+1_
  #   i - line number
  #   j - station number
  #
  # entry_times[i]: how much time it takes to enter line _i_
  #
  # exit_times[i]: how much time it takes to exit line _i_
  #
  def initialize(assembly_times, transfer_times, entry_times, exit_times)
    @line_count = assembly_times.size
    @station_count = assembly_times[0].size

    # Create data storages
    @time = Array.new(@line_count) { [] }
    @lines = Array.new(@line_count) { [] }
    
    # Calc first station times.
    @line_count.times { |line| @time[line][0] = entry_times[line] + assembly_times[line][0] }

    # Calc other station times.
    1.upto(@station_count - 1) do |j|
      stay_time = @time[0][j - 1] + assembly_times[0][j]
      switch_time = @time[1][j - 1] + transfer_times[1][j - 1] + assembly_times[0][j]
      if stay_time <= switch_time
        @time[0][j] = stay_time
        @lines[0][j] = 0
      else
        @time[0][j] = switch_time
        @lines[0][j] = 1
      end

      stay_time = @time[1][j - 1] + assembly_times[1][j]
      switch_time = @time[0][j - 1] + transfer_times[0][j - 1] + assembly_times[1][j]
      if stay_time <= switch_time
        @time[1][j] = stay_time
        @lines[1][j] = 1
      else
        @time[1][j] = switch_time
        @lines[1][j] = 0
      end
    end

    # Finally calc exit times.
    exit_time_0 = @time[0][@station_count - 1] + exit_times[0]
    exit_time_1 = @time[1][@station_count - 1] + exit_times[1]
    if exit_time_0 <= exit_time_1
      @exit_time = exit_time_0
      @exit_line = 0
    else
      @exit_time = exit_time_1
      @exit_line = 1
    end
  end

  def fastest_path
    path = []

    current_line = @exit_line
    end_station = @station_count - 1
    path.push [current_line, end_station]

    end_station.downto(1) do |station|
      current_line = @lines[current_line][station]
      path.push [current_line, station - 1]
    end

    path.reverse
  end
end

if __FILE__ == $0
  assembly_times = [
    [7, 9, 3, 7, 8, 4],
    [8, 5, 6, 4, 5, 7]
  ]
  transfer_times = [
    [2, 3, 1, 3, 4],
    [2, 1, 2, 2, 1]
  ]
  entry_times = [2, 4]
  exit_times = [3, 2]

  al = AssemblyLine.new(assembly_times, transfer_times, entry_times, exit_times)
  puts "Fastest path:"
  al.fastest_path.each do |line, station, time|
    puts "  line #{line + 1}, station #{station + 1}"
  end
end