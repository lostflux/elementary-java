#=
trial:
- Julia version: 
- Author: amitt
- Date: 2021-01-22
=#

sum(x, y) = x + y
println(sum(2, 3))

add = 0
for i in 1:100
    global add += i
    println(add)
end
println(add)

