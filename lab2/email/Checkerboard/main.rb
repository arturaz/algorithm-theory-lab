require 'benchmark'

class Checkerboard
  attr_reader :scores, :came_from

  def initialize(checkerboard)
    @length = checkerboard.size
    @scores = checkerboard
    @came_from = Array.new(@length) { Array.new(@length, nil) }

    max_index = @length - 1
    # O(n^2)
    1.upto(max_index) do |x|
      0.upto(max_index) do |y|
        possible_y = [y]
        possible_y.push y - 1 if y > 0
        possible_y.push y + 1 if y < max_index

        max_addon_score = 0
        came_from = nil

        # We can say O(1) here
        possible_y.each do |lower_level_y|
          $operations += 2
          addon_score = @scores[x - 1][lower_level_y]
          if addon_score > max_addon_score
            $operations += 2
            max_addon_score = addon_score
            came_from = lower_level_y
          end
        end

        $operations += 2
        @scores[x][y] = checkerboard[x][y] + max_addon_score
        @came_from[x][y] = came_from
      end
    end
  end

  def best_path
    max = 0
    best_y = nil

    # Find best value
    0.upto(@length - 1) do |current_y|
      score = @scores[@length - 1][current_y]
      if score > max
        max = score
        best_y = current_y
      end
    end

    path = []
    y = best_y
    (@length - 1).downto(0) do |x|
      path.push [x, y, @scores[x][y]]
      y = @came_from[x][y]
    end

    path.reverse
  end
end

if __FILE__ == $0
  srand(300)

  checkerboard = nil
  cb = nil
  100.step(1000, 100) do |size|
    checkerboard = Array.new(size) {
      Array.new(size) {
        rand(9)
      }
    }
    # O(n^2)
    $operations = 0
    time = Benchmark.realtime { cb = Checkerboard.new(checkerboard) }
    puts "%10d %10d %8.3f" % [size, $operations, time]
  end

#  puts "Best path:"
#  cb.best_path.each do |x, y, score|
#    puts "X: #{x + 1}, Y: #{y + 1}, score: #{score}"
#  end
end