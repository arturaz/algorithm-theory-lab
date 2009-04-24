require 'pp'

class Checkerboard
  attr_reader :scores, :came_from

  def initialize(checkerboard)
    @length = checkerboard.size
    @scores = checkerboard
    @came_from = Array.new(@length) { Array.new(@length, nil) }

    max_index = @length - 1
    1.upto(max_index) do |x|
      0.upto(max_index) do |y|
        possible_y = [y]
        possible_y.push y - 1 if y > 0
        possible_y.push y + 1 if y < max_index

        max_addon_score = 0
        came_from = nil

        possible_y.each do |lower_level_y|
          addon_score = @scores[x - 1][lower_level_y]
          if addon_score > max_addon_score
            max_addon_score = addon_score
            came_from = lower_level_y
          end
        end

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
  checkerboard = [
    [1, 3, 4, 7, 8, 9],
    [2, 2, 1, 6, 2, 0],
    [-4, 1, -8, 8, -1, -4],
    [-3, 7, -2, 1, -8, -3],
    [-7, 2, 6, 2, -2, -1],
    [-8, 2, -4, 4, 7, 3],
  ]

  cb = Checkerboard.new(checkerboard)
  
  puts "Scores:"
  pp cb.scores

  puts "\nCame from:"
  pp cb.came_from

  puts "Best path:"
  cb.best_path.each do |x, y, score|
    puts "X: #{x + 1}, Y: #{y + 1}, score: #{score}"
  end
end