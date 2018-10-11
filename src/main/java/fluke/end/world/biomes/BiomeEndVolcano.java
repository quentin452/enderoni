package fluke.end.world.biomes;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeEndDecorator;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import fluke.end.block.ModBlocks;
import fluke.end.world.feature.WorldGenEndCactus;
import fluke.end.world.feature.WorldGenEndLake;
import fluke.end.world.feature.WorldGenEndVolcano;

public class BiomeEndVolcano extends Biome
{
	public static BiomeProperties properties = new BiomeProperties("End Volcano");
	public WorldGenerator endVolcano = new WorldGenEndVolcano(ModBlocks.endObsidian.getDefaultState(), ModBlocks.endMagma.getDefaultState(), ModBlocks.endAcid.getDefaultState());
	public WorldGenerator endSmallLake = new WorldGenLakes(ModBlocks.endAcid);
	public WorldGenerator endLargeLake = new WorldGenEndLake(ModBlocks.endAcid.getDefaultState(), ModBlocks.endObsidian.getDefaultState());
	public WorldGenerator endCactus = new WorldGenEndCactus();
	private static final IBlockState AIR = Blocks.AIR.getDefaultState();
	private static final IBlockState END_STONE = Blocks.END_STONE.getDefaultState();
	private static final IBlockState END_OBSIDIAN = ModBlocks.endObsidian.getDefaultState();
	private static final IBlockState END_MAGMA = ModBlocks.endMagma.getDefaultState();
	private Random randy;
	
	static {
		properties.setTemperature(Biomes.SKY.getDefaultTemperature());
		properties.setRainfall(Biomes.SKY.getRainfall());
		properties.setRainDisabled();
	}
	
    public BiomeEndVolcano()
    {
        super(properties);
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityEnderman.class, 10, 4, 4));
        this.topBlock = END_OBSIDIAN;
        this.fillerBlock = END_OBSIDIAN;
        this.decorator = new BiomeEndDecorator();
        randy = new Random();
    }
    
    @Override
	public BiomeDecorator createBiomeDecorator()
    {
		return new BiomeDecoratorEndBiomes();
	}

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float currentTemperature)
    {
        return 0;
    }
    
    public void decorate(World world, Random rand, BlockPos pos)
    {
    	if(randy.nextInt(8) == 0)
		{
			int yHeight = getEndSurfaceHeight(world, pos.add(16, 0, 16), 50, 70, null);
			if(yHeight > 0)
				endVolcano.generate(world, rand, pos.add(16, yHeight+1, 16));
		}
    	
    	if(randy.nextInt(10) == 0)
    	{
    		int randX = rand.nextInt(8)+12;
    		int randZ = rand.nextInt(8)+12;
    		int yHeight = getEndSurfaceHeight(world, pos.add(randX, 0, randZ), 60, 70, null);
			if(yHeight > 0)
				endLargeLake.generate(world, rand, pos.add(randX, yHeight, randZ));
    	}
    	
    	if(randy.nextInt(1) == 0)
    	{
    		int randX = rand.nextInt(16)+8;
    		int randZ = rand.nextInt(16)+8;
    		int yHeight = getEndSurfaceHeight(world, pos.add(randX, 0, randZ), 50, 70, null);
			if(yHeight > 0)
				endCactus.generate(world, rand, pos.add(randX, yHeight, randZ));
    	}
    	
    	if(randy.nextInt(8) == 0)
    	{
    		int randX = rand.nextInt(16)+8;
    		int randZ = rand.nextInt(16)+8;
    		int yHeight = getEndSurfaceHeight(world, pos.add(randX, 0, randZ), 60, 70, null);
			if(yHeight > 0)
				endSmallLake.generate(world, rand, pos.add(randX, yHeight, randZ));
    	}
		
		super.decorate(world, rand, pos);
    }
    
    private int getEndSurfaceHeight(World world, BlockPos pos, int min, int max, IBlockState specificBlock)
    {
    	int maxY = max;
    	int minY = min;
    	int currentY = maxY;
    	IBlockState state;
    	
    	while(currentY >= minY)
    	{
    		state = world.getBlockState(pos.add(0, currentY, 0));
    		if(state != AIR)
    			if(specificBlock == null || state == specificBlock)
    				return currentY;
    		
    		currentY--;
    	}
    	return 0;
    }
}
