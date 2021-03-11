"""
    what()

- Julia version: Unknown
- Author: amitt
- Date: 2021-02-09

# Arguments

# Examples

```jldoctest
julia>
```
"""
a = 4 + 3im
b = conj(a)
c = cis(pi)
println(+(1, 2, 3, 4, 5))
println(2 > 3 && println("I'm lazy"))

z = NaN
if isnan(z)
    println(z)
end