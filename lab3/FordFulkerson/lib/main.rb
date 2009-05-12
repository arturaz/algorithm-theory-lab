class FlowNetwork
  def initialize
    @adjency = {}
    @flow = {}
  end

  def add_vertex(vertex)
    @adjency[vertex] = []
  end

  def get_edges(vertex)
    @adjency[vertex]
  end

  def add_edge(source_vertex, vertex, capacity=0)
    @adjency[source_vertex].push([vertex, capacity])
    @adjency[vertex].push([source_vertex, 0])
    @flow[ [source_vertex, vertex] ] = 0
    @flow[ [vertex, source_vertex] ] = 0
  end

  def find_path(source, sink, path=[])
    $operations += 1
    return path if source == sink

    $operations += 1
    get_edges(source).each do |vertex, capacity|
      $operations += 3
      residual = capacity - @flow[ [source, vertex] ]
      edge = [source, vertex, residual]
      if residual > 0 and not path.include?(edge)
        $operations += 2
        result = find_path(vertex, sink, path + [edge])
        return result unless result.nil?
      end
    end

    nil
  end

  def max_flow(source, sink)
    $operations += 1
    path = find_path(source, sink)
    until path.nil?
      $operations += 2
      flow = path.map { |source_vertex, vertex, residual| residual }.min
      path.each do |source_vertex, vertex, residual|
        $operations += 2
        @flow[ [source_vertex, vertex] ] += flow
        @flow[ [vertex, source_vertex] ] -= flow
      end
      $operations += 1
      path = find_path(source, sink)
    end

    # Return sum of flow
    $operations += 1
    get_edges(source).inject(0) do |sum, data|
      sum + @flow[ [source, data[0]] ]
    end
  end
end

if __FILE__ == $0
  require 'benchmark'
  srand(300)

  100.step(3000, 300) do |vertexes|
    edges = 0

    g = FlowNetwork.new
    g.add_vertex :source
    g.add_vertex :target

    last_vertex = nil
    vertexes.times do |i|
      name = :"vertex#{i}"
      g.add_vertex name

      edges += 2
      g.add_edge(:source, name, 20 + i % 10) # From source to this
      g.add_edge(name, :target, 20 + i % 10) # From this to target
      # From last vertex to this
      unless last_vertex.nil?
        edges += 1
        g.add_edge(last_vertex, name, 20 + i % 10)
      end

      last_vertex = name
    end

    $operations = 0
    time = Benchmark.realtime { g.max_flow(:source, :target) }
    puts "%10d %10d %10d %8.3f" % [vertexes, edges, $operations, time]
    $stdout.flush
  end
end
