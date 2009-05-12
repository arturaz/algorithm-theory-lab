class Vertex
  attr_reader :number, :neighbors
  attr_accessor :visited, :num, :low, :parent

  def initialize(number, *neighbors)
    @number = number
    @neighbors = []

    connect(*neighbors)
  end

  def connect(*vertexes)
    vertexes.each do |vertex|
      raise ArgumentError.new("vertex must be Vertex not #{vertex.class}!") \
        unless vertex.is_a?(Vertex)
      @neighbors.push vertex unless @neighbors.include? vertex
    end
  end

  def to_s
    " %-4d: %s (visited: %s, num: %d, low: %d)" % [
      @number,
      @neighbors.map { |v| v.number }.join(", "),
      @visited,
      @num,
      @low
    ]
  end
end

class Graph
  def initialize(title, bidirectional=true)
    @title = title
    @bidirectional = bidirectional
    @vertexes = {}

    @root = nil
  end

  def [](number)
    @vertexes[number]
  end

  # Assign edges. Create vertexes as necessary
  def []=(number, neighbors)
    neighbors = [neighbors].flatten
    init_vertexes([number] + neighbors)
    
    @root ||= @vertexes[number]
    neighbors.each do |n|
      @vertexes[number].connect @vertexes[n]
      @vertexes[n].connect @vertexes[number] if @bidirectional
    end
  end

#  def find_art(vertex)
#    counter = 0
#    apoints = []
#    root = vertex
#    root_children = []
#
#    to_visit = [vertex]
#    walking_tree = []
#
#    while vertex = to_visit.pop
#      walking_tree.push(vertex)
#      $operations += 3
#      vertex.visited = true
#      counter += 1
#      vertex.low = vertex.num = counter # Rule 1
#
#      # O(n)
#      vertex.neighbors.each do |neighbor|
#        unless neighbor.visited or to_visit.include?(neighbor)
#          to_visit.push(neighbor)
#        end
#      end
##      vertex.neighbors.each do |neighbor|
##        if not neighbor.visited # Forward edge
##          $operations += 4
##          neighbor.parent = vertex
##          if vertex == root
##            $operations += 1
##            root_children.push neighbor
##          end
##
##          puts "Unshifting #{neighbor}"
##          to_visit.unshift neighbor unless to_visit.include? neighbor
##
##          to_compute.unshift [vertex, neighbor, :forward]
##        elsif vertex.parent != neighbor # Back edge
##          to_compute.unshift [vertex, neighbor, :back]
##        end
##      end
#
#      puts "Stack: #{to_visit.map { |i| i.number }.inspect}"
#    end
#
#    puts "WTREE: #{walking_tree.map { |i| i.number }.inspect}"
#
##    to_compute.each do |vertex, neighbor, type|
##      case type
##      when :forward
##        $operations += 2
##        if vertex != root and neighbor.low >= vertex.num
##          puts "Vertex: #{vertex}, root: #{root}"
##          apoints.push vertex
##        end
##        vertex.low = [vertex.low, neighbor.low].min # Rule 3
##      when :back
##        $operations += 1
##        vertex.low = [vertex.low, neighbor.num].min # Rule 2
##      end
##    end
#
#    puts "ROOT CHILDREN: #{root_children}"
#    apoints.push root if root_children.size >= 2
#    apoints
#  end

  def find_art(vertex)
    @counter = 0
    @apoints = []
    @root = vertex
    @root_children = []
    find_art_recursive(vertex)
    @apoints.push @root if @root_children.size >= 2
    @apoints
  end

  def find_art_recursive(vertex)
    $operations += 3
    vertex.visited = true
    @counter += 1
    vertex.low = vertex.num = @counter


    # O(n)
    vertex.neighbors.each do |neighbor|
      if not neighbor.visited # Forward edge
        $operations += 4
        neighbor.parent = vertex
        if vertex == @root
          $operations += 1
          @root_children.push neighbor
        end

        find_art_recursive(neighbor)

        $operations += 2
        @apoints.push vertex if vertex != @root and neighbor.low >= vertex.num
        vertex.low = [vertex.low, neighbor.low].min # Rule 3
      elsif vertex.parent != neighbor # Back edge
        $operations += 2
        vertex.low = [vertex.low, neighbor.num].min # Rule 2
      end
    end
  end

  def to_s
    s = "Graph '%s' of %d vertexes (%s, root:[%s]):\n" % [
      @title,
      @vertexes.size,
      @bidirectional ? "undirected" : "directed",
      @root
    ]
    @vertexes.keys.sort.each { |number| s += "#{@vertexes[number]}\n" }
    s
  end

  def init_vertexes(*numbers)
    numbers.flatten.each { |number| @vertexes[number] ||= Vertex.new(number) }
  end
end

if __FILE__ == $0
  require 'benchmark'

#  g = Graph.new "test"
#  g[0] = [1, 2]
#  g[3] = [1, 2, 4]
#  g[2] = [5, 6]
#  g[5] = 6
#
#  puts g
#  puts ""
#
#  $operations = 0
#  puts g.find_art(g[0])
#
#  puts ""
#  puts g

  10.step(30000, 1) do |vertexes|
    edges = 0

    g = Graph.new "#{vertexes}"

    last_vertexes = []
    vertexes.times do |i|
      g.init_vertexes(i)

      edges += last_vertexes.size
      g[i] = last_vertexes

      # Keep last 5
      last_vertexes = ([i] + last_vertexes).slice(0, 5)
    end

    $operations = 0
    time = Benchmark.realtime { g.find_art(g[0]) }
    puts "%10d %10d %10d %8.3f" % [vertexes, edges, $operations, time]
    $stdout.flush
  end
end