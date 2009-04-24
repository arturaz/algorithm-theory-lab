#!/usr/bin/env ruby
DEST = "data"

if ARGV[0] == 'gen'
  require 'fileutils'

  STEP = (ARGV[2] || 30000).to_i
  MAX = (ARGV[1] || 20).to_i

  FileUtils.rm_r(DEST) if File.exists?(DEST)
  FileUtils.mkdir_p(DEST)

  puts "Generating #{MAX} times with #{STEP} step"
  1.upto(MAX) do |i|
    num = i * STEP
    cmd = "randomize.exe -n#{num} > #{DEST}/#{num}.txt"
    puts cmd
    `#{cmd}`
  end
elsif ARGV[0] == 'run'
  run = ARGV[1] == 'java' \
    ? "java -Xms256m -Xmx1000m -jar algo-java.jar" \
    : "dist/Debug/Cygwin-Linux-x86/cpp.exe"
  ARGV[2].split(",").each do |storage|
    ARGV[3].split(",").each do |algorithm|
      Dir.glob("#{DEST}/*").each do |data|
        cmd = "#{run} #{storage} #{algorithm} #{data}"
        system cmd
      end
    end
  end
else
  puts "Usage: #{$0} gen|run"
  puts "  gen [times] [step]"
  puts "  run java|cpp storage1[,storage2,...] algorithm1[,algorithm2,...]"
end
